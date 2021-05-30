package com.vaibhav.sociofy.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.sociofy.data.models.remote.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val fcm: FirebaseMessaging,
) {

    fun isLoggedIn() = auth.currentUser != null

    fun logOut() {
        auth.signOut()
    }

    fun getCurrentUserId() = auth.currentUser!!.uid


    fun loginUser(
        email: String,
        password: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                getToken({
                    addUserTokenToFireStore(it, successListener, failureListener)
                }, failureListener = {
                    failureListener(it)
                })
            }
            .addOnFailureListener { exception ->
                failureListener.invoke(exception)
            }
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (isLoggedIn()) {
                    getToken(successListener = {
                        Timber.d(it)
                        addUserToFireStore(username, token = it, successListener = {
                            successListener.invoke()
                        }, failureListener = { failureListener(it) })
                    }, failureListener = { failureListener.invoke(it) })

                }
            }
            .addOnFailureListener { task ->
                failureListener.invoke(task)
            }

    }

    private fun getToken(successListener: (String) -> Unit, failureListener: (Exception) -> Unit) {
        fcm.token
            .addOnSuccessListener {
                successListener(it)
            }
            .addOnFailureListener { failureListener(it) }
    }

    suspend fun updateUser(
        username: String,
        bio: String,
        image: Uri?,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            image?.let {
                storeImage(image, successListener = {
                    updateUerInFireStore(username, bio, it, successListener, failureListener)
                }, failureListener = {
                    failureListener.invoke(it)
                })
            } ?: updateUserInFireStoreWithoutImage(username, bio, successListener, failureListener)

        }
    }

    private fun updateUserInFireStoreWithoutImage(
        username: String,
        bio: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        fireStore.collection("users").document(getCurrentUserId())
            .update("username", username, "bio", bio)
            .addOnSuccessListener {
                successListener.invoke()
            }
            .addOnFailureListener { failureListener.invoke(it) }
    }

    private fun updateUerInFireStore(
        username: String,
        bio: String,
        image: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        fireStore.collection("users").document(getCurrentUserId())
            .update("username", username, "bio", bio, "profileImg", image)
            .addOnSuccessListener {
                successListener.invoke()
            }
            .addOnFailureListener { failureListener.invoke(it) }
    }

    private fun storeImage(
        image: Uri,
        successListener: (String) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        val filename = getCurrentUserId()
        storage.reference.child(filename).putFile(image)
            .addOnSuccessListener {
                getDownloadUrl(filename, successListener = {
                    successListener.invoke(it)
                }, failureListener = { failureListener.invoke(it) })
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }


    private fun getDownloadUrl(
        filename: String,
        successListener: (String) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        storage.reference.child(filename).downloadUrl
            .addOnSuccessListener {
                successListener.invoke(it.toString())
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }


    suspend fun setUserImage(
        uri: Uri,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            addUserImageToStorage(uri, successListener, failureListener)

        }
    }

    private suspend fun addUserImageToStorage(
        uri: Uri,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        val filename = getCurrentUserId()
        withContext(Dispatchers.IO)
        {
            storage.reference.child(filename).putFile(uri)
                .addOnSuccessListener {
                    getUserImageUrl(filename, successListener, failureListener)
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }
    }

    private fun getUserImageUrl(
        filename: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        storage.reference.child(filename).downloadUrl
            .addOnSuccessListener { url ->
                addUrlToUserInFireStore(url.toString(), successListener, failureListener)
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }

    private fun addUrlToUserInFireStore(
        url: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        auth.currentUser?.let {
            fireStore.collection("users").document(it.uid).update("profileImg", url)
                .addOnSuccessListener {
                    successListener.invoke()
                }
                .addOnFailureListener { exception ->
                    failureListener.invoke(exception)
                }
        }

    }

    private fun addUserTokenToFireStore(
        token: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        val userId = getCurrentUserId()
        fireStore.collection("users").document(userId).update("deviceToken", token)
            .addOnSuccessListener { successListener() }
            .addOnFailureListener { failureListener(it) }

    }

    private fun addUserToFireStore(
        username: String,
        token: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {

        auth.currentUser?.let {
            val user = User(it.uid, username, it.email!!)
            user.deviceToken = token
            Timber.d(user.toString())
            fireStore.collection("users").document(user.id).set(user)
                .addOnSuccessListener { successListener() }
                .addOnFailureListener { failureListener(it) }
        }
    }

    private fun updateUserName(username: String, firebaseUser: FirebaseUser) {
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(username)
        firebaseUser.updateProfile(userProfileChangeRequest.build())
        Timber.d(firebaseUser.displayName)
    }

    suspend fun getCurrentUserDetails(
        successListener: (User) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO)
        {
            fireStore.collection("users").document(getCurrentUserId())
                .addSnapshotListener { users, error ->
                    if (error != null) {
                        failureListener.invoke(error)
                        return@addSnapshotListener
                    }
                    users?.let {
                        val user = users.toObject<User>()
                        Timber.d(user.toString())
                        successListener.invoke(user!!)
                    } ?: failureListener.invoke(Exception("User not found"))

                }
        }
    }

    suspend fun getAllUsers(
        successListener: (List<User>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection("users").addSnapshotListener { value, error ->
                if (error != null) {
                    failureListener.invoke(error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = mutableListOf<User>()
                    for (user in value.documents) {
                        val us = user.toObject<User>()
                        us?.let {
                            if (us.id != getCurrentUserId())
                                list.add(us)
                        }
                    }
                    successListener.invoke(list)
                    return@addSnapshotListener
                }
            }
        }
    }

    suspend fun followUser(
        currentUser: User,
        userToBeFollowed: User,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val followingMap = userToBeFollowed.followers
            followingMap[currentUser.id] = currentUser.deviceToken
            val followerMap = currentUser.following
            followerMap[userToBeFollowed.id] = userToBeFollowed.deviceToken
            fireStore.collection("users").document(currentUser.id).update("following", followerMap)
                .addOnSuccessListener {
                    fireStore.collection("users").document(userToBeFollowed.id)
                        .update("followers", followingMap)
                        .addOnSuccessListener {
                            successListener.invoke()
                        }
                        .addOnFailureListener { failureListener.invoke(it) }
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }
    }

    suspend fun unFollowUser(
        currentUser: User,
        userToBeFollowed: User,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val followingMap = userToBeFollowed.followers
            followingMap.remove(currentUser.id)
            val followerMap = currentUser.following
            followerMap.remove(userToBeFollowed.id)
            fireStore.collection("users").document(currentUser.id).update("following", followerMap)
                .addOnSuccessListener {
                    fireStore.collection("users").document(userToBeFollowed.id)
                        .update("followers", followingMap)
                        .addOnSuccessListener {
                            successListener.invoke()
                        }
                        .addOnFailureListener { failureListener.invoke(it) }
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }
    }


    suspend fun getUsersByFilter(
        following: MutableMap<String, String>,
        successListener: (List<User>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val followingList = following.map { it.key }

            fireStore.collection("users").whereIn("id", followingList)
                .get()
                .addOnSuccessListener {
                    val lis = mutableListOf<User>()
                    for (doc in it.documents) {
                        val user = doc.toObject<User>()
                        user?.let {
                            lis.add(user)
                        }
                    }

                    successListener.invoke(lis)
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }
    }


}