package com.vaibhav.sociofy.data.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repository.Preferences
import com.vaibhav.sociofy.util.Shared.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val fcm: FirebaseMessaging,
    private val sharedPref: Preferences
) {

    companion object {
        const val USER_COLLECTION = "users"
    }

    fun isUserLoggedIn() = auth.currentUser != null

    fun getUserDetails() = sharedPref.getUserData()

    fun logoutUser() {
        sharedPref.removeUserData()
        auth.signOut()
    }

    suspend fun loginUser(email: String, password: String) = flow<Resource<User>> {
        emit(Resource.Loading())
        val response = auth.signInWithEmailAndPassword(email, password).await()
        response.user?.let {
            val userId = it.uid
            val user = getUserDataFromFireStore(userId)
            sharedPref.saveUserData(user = user.data!!)
            emit(user)
        } ?: emit(Resource.Error())
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }

    suspend fun registerUser(
        email: String,
        username: String,
        password: String
    ) = flow<Resource<User>> {
        emit(Resource.Loading())
        val response = auth.createUserWithEmailAndPassword(email, password).await()
        response.user?.let {
            val token = getUserFcmToken()
            val user = User(id = it.uid, username = username, email = email)
            user.deviceToken = token
            saveUserInFireStore(user)
            sharedPref.saveUserData(user)
            emit(Resource.Success(user))
        } ?: emit(Resource.Error())
    }.catch {
        auth.signOut()
        emit(Resource.Error(it.message.toString()))
    }

    suspend fun getUserDataFromFireStore(userId: String): Resource<User> {
        val user =
            fireStore.collection(USER_COLLECTION).document(userId).get().await().toObject<User>()
        return user?.let {
            Resource.Success(it)
        } ?: Resource.Error()

    }


    suspend fun updateUser(image: Uri, username: String, bio: String) =
        flow<Resource<Unit>> {
            emit(Resource.Loading())
            val user = getUserDetails()
            val imageUrl = uploadUserImageToStorage(image, user.id)
            user.bio = bio
            user.profileImg = imageUrl
            user.username = username
            updateUserDetailsInFireStore(user)
            sharedPref.saveUserData(user)
            emit(Resource.Success())
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    private suspend fun updateUserDetailsInFireStore(user: User) {
        fireStore.collection(USER_COLLECTION).document(user.id)
            .update("username", user.username, "bio", user.bio, "profileImg", user.profileImg)
            .await()
    }

    private suspend fun saveUserInFireStore(user: User) =
        fireStore.collection(USER_COLLECTION).document(user.id).set(user).await()

    private suspend fun getUserFcmToken(): String = fcm.token.await()

    private suspend fun uploadUserImageToStorage(image: Uri, filename: String): String {
        val response = storage.reference.child(filename).putFile(image).await()
        return storage.reference.child(filename).downloadUrl.await().toString()
    }

    suspend fun getAllUsers() {
        val users = fireStore.collection(USER_COLLECTION).get().await().toObjects(User::class.java)
    }

    @ExperimentalCoroutinesApi
    fun getUserDetails(userId: String) = callbackFlow<Resource<User>> {
        fireStore.collection(USER_COLLECTION).document(userId)
            .addSnapshotListener { value, error ->
                error?.let {
                    offer(Resource.Error(it.message.toString()))
                } ?: value?.let {
                    offer(Resource.Success(it.toObject<User>()))
                } ?: offer(Resource.Error())
            }
    }

    suspend fun followUser(userToBeFollowed: User) = flow<Resource<Unit>> {
        emit(Resource.Loading())
        val currentUser = getUserDetails()
        userToBeFollowed.followers[currentUser.id] = currentUser.deviceToken
        currentUser.following[userToBeFollowed.id] = userToBeFollowed.deviceToken
        fireStore.collection(USER_COLLECTION).document(currentUser.id)
            .update("following", currentUser.following).await()
        fireStore.collection(USER_COLLECTION).document(userToBeFollowed.id)
            .update("follower", userToBeFollowed.followers).await()
        emit(Resource.Success())
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }

    suspend fun unFollowUser(userToBeUnFollowed: User) = flow<Resource<Unit>> {
        emit(Resource.Loading())
        val currentUser = getUserDetails()
        userToBeUnFollowed.followers.remove(currentUser.id)
        currentUser.following.remove(userToBeUnFollowed.id)
        fireStore.collection(USER_COLLECTION).document(currentUser.id)
            .update("following", currentUser.following).await()
        fireStore.collection(USER_COLLECTION).document(userToBeUnFollowed.id)
            .update("follower", userToBeUnFollowed.followers).await()
        emit(Resource.Success())
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }


    fun getUsersByFilter(following: MutableMap<String, String>) = flow<Resource<List<User>>> {
        emit(Resource.Loading())
        val followingList = following.map { it.key }
        val users = fireStore.collection(USER_COLLECTION).whereIn("id", followingList)
            .get()
            .await()
            .toObjects(User::class.java)
        emit(Resource.Success(users))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }

}