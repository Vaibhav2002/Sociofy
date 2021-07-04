package com.vaibhav.sociofy.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.sociofy.data.local.downloadedPost.DownloadedPostDao
import com.vaibhav.sociofy.data.models.*
import com.vaibhav.sociofy.data.models.local.DownloadedPost
import com.vaibhav.sociofy.data.remote.Api
import com.vaibhav.sociofy.util.Shared.urlToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val downloadedPostDao: DownloadedPostDao,
    private val context: Context,
    private val api: Api
) {


    suspend fun getFeedOfFollowers(
        successListener: (MutableList<Post>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            authRepository.getCurrentUserDetails({
                fireStore.collection("posts")
                    .addSnapshotListener { posts, error ->
                        val postsList: MutableList<Post> = mutableListOf()
                        if (error != null) {
                            failureListener.invoke(error)
                        } else {
                            for (po in posts!!.documents) {
                                val pos = po.toObject<Post>()!!
                                if (pos.uid != auth.currentUser!!.uid && it.following.keys.contains(
                                        pos.uid
                                    )
                                )
                                    postsList.add(pos)
                            }
                            successListener.invoke(postsList)
                        }
                    }
            }, failureListener = {
                failureListener.invoke(it)
            })
        }
    }

    suspend fun getAllFeedPosts(
        successListener: (MutableList<Post>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection("posts")
                .addSnapshotListener { posts, error ->
                    val postsList: MutableList<Post> = mutableListOf()
                    if (error != null) {
                        failureListener.invoke(error)
                    } else {
                        for (po in posts!!.documents) {
                            val pos = po.toObject<Post>()!!
                            if (pos.uid != auth.currentUser!!.uid)
                                postsList.add(pos)
                        }
                        successListener.invoke(postsList)
                    }
                }
        }
    }

    suspend fun getUsersPosts(
        uid: String,
        successListener: (List<Post>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection("posts").addSnapshotListener { value, error ->
                val list = mutableListOf<Post>()
                if (error != null)
                    failureListener.invoke(error)
                else {
                    value?.let {
                        for (post in value.documents) {
                            val po = post.toObject<Post>()!!
                            if (po.uid == uid)
                                list.add(po)
                        }
                        successListener.invoke(list)
                    }
                }
            }
        }
    }

    suspend fun getPostFromId(
        id: String,
        successListener: (Post) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO)
        {
            fireStore.collection("posts").document(id).get()
                .addOnSuccessListener { post ->
                    if (post.exists() && post != null)
                        successListener.invoke(post.toObject<Post>()!!)
                    else
                        failureListener.invoke(Exception("Post not found"))
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }
    }


    suspend fun uploadInStorage(
        uri: Uri?,
        successListener: (String) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val filename = auth.currentUser!!.uid + System.currentTimeMillis().toString()
            storage.reference.child(filename).putFile(uri!!)
                .addOnSuccessListener {
                    successListener.invoke(filename)
                }
                .addOnFailureListener {
                    failureListener.invoke(it)
                }
        }

    }

    private fun getProfileImageUrl(
        successListener: (String) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        storage.reference.child(auth.currentUser!!.uid).downloadUrl
            .addOnSuccessListener { url ->
                successListener.invoke(url.toString())
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }

    private fun getPostImageUri(
        filename: String,
        successListener: (String) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        storage.reference.child(filename).downloadUrl.addOnSuccessListener { url ->
            successListener.invoke(url.toString())
        }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }


    suspend fun uploadPost(
        filename: String,
        description: String,
        user: User,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            getProfileImageUrl(successListener = { url ->
                getPostImageUri(filename, successListener = { posturl ->
                    val post = Post(
                        posturl,
                        description,
                        user.username,
                        url,
                        user.id,
                    )
                    val notification = Notification(
                        post.uid,
                        post.postUid,
                        posturl,
                        url,
                        post.username,
                        post.timeStamp
                    )
                    postNotification(notification)
                    fireStore.collection("posts")
                        .document(post.postUid).set(post)
                        .addOnSuccessListener {
                            successListener.invoke()
                        }
                        .addOnFailureListener {
                            failureListener.invoke(it)
                        }
                }, failureListener = {
                    failureListener.invoke(it)
                })
            }, failureListener = {
                failureListener.invoke(it)
            })
        }
    }

    suspend fun sendPushNotification(message: String, user: User) =
        withContext(Dispatchers.IO) {
            val title = "${user.username} posted a new image"
            for (token in user.followers.values) {
                Timber.d(token)
                PushNotification(
                    data = PushNotificationItem(title = title, description = message),
                    to = token
                ).also {
                    val response = api.postNotification(it)
                    if (response.isSuccessful)
                        Timber.d("sucess")
                    else
                        Timber.d("failed")
                }
            }
        }

    private fun postNotification(notification: Notification) {
        fireStore.collection("notifications").document().set(notification)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful)
                    Timber.d("Notification failed")
            }
    }

    suspend fun getNotifications(
        successListener: (List<Notification>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            authRepository.getCurrentUserDetails(successListener = {
                fireStore.collection("notifications").addSnapshotListener { value, error ->
                    val list = mutableListOf<Notification>()
                    if (error != null)
                        failureListener.invoke(error)
                    else {
                        value?.let { _ ->
                            for (not in value.documents) {
                                val notification = not.toObject<Notification>()
                                if (notification!!.uid != auth.currentUser!!.uid && notification.uid in it.following.keys)
                                    list.add(notification)
                            }
                        }
                        list.sortByDescending { it.timestamp }
                        successListener.invoke(list)
                    }
                }
            }, failureListener = {
                failureListener.invoke(it)
            })

        }

    }


    suspend fun likePost(
        post: Post,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val likes = post.likes + 1
            val likedBy = post.likedBy
            likedBy[auth.currentUser!!.uid] = true
            fireStore.collection("posts").document(post.postUid)
                .update("likes", likes, "likedBy", likedBy)
                .addOnSuccessListener { successListener.invoke() }
                .addOnFailureListener { failureListener.invoke(it) }
        }
    }

    suspend fun dislikePost(
        post: Post,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val likes = post.likes - 1
            val likedBy = post.likedBy
            likedBy.remove(auth.currentUser!!.uid)
            fireStore.collection("posts").document(post.postUid)
                .update("likes", likes, "likedBy", likedBy)
                .addOnSuccessListener { successListener.invoke() }
                .addOnFailureListener { failureListener.invoke(it) }
        }
    }


    fun savePost(
        userId: String,
        postId: String,
        successListener: (Saved) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        try {
            Timber.d("InSaveRepo")
            val save = Saved(userId, postId)
            fireStore.collection("saved").document(save.saveId)
                .set(save)
                .addOnSuccessListener { successListener(save) }
                .addOnFailureListener { failureListener(it) }
        } catch (e: Exception) {
            failureListener(e)
        }

    }

    suspend fun getSavedPosts(
        userId: String,
        onSuccessListener: (List<SavedPosts>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            fireStore.collection("saved").whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { doc ->
                    val savedList = mutableListOf<Saved>()
                    val savedListIds = mutableListOf<String>()
                    for (saved in doc.documents) {
                        saved.toObject(Saved::class.java)?.let {
                            savedList.add(it)
                            savedListIds.add(it.postId)
                        }
                    }
                    Timber.d(savedList.toString())
                    if (savedListIds.isEmpty())
                        onSuccessListener(emptyList())
                    else
                        getPostFromSaveIds(
                            savedListIds,
                            onSuccessListener,
                            onFailureListener
                        )
                }
                .addOnFailureListener(onFailureListener)
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }

    private fun getPostFromSaveIds(
        savedListIds: MutableList<String>,
        onSuccessListener: (List<SavedPosts>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val savedPosts = mutableListOf<SavedPosts>()
        fireStore.collection("posts").whereIn("postUid", savedListIds)
            .get()
            .addOnSuccessListener { docs ->
                for (document in docs.documents) {
                    document.toObject(Post::class.java)?.let { post ->
                        savedPosts.add(
                            SavedPosts(
                                post = post,
                                timeStamp = post.timeStamp.toString()
                            )
                        )
                    }
                }
                Timber.d(savedPosts.toString())
                onSuccessListener(savedPosts)
            }
            .addOnFailureListener(onFailureListener)
    }


    //local

    fun getDownloadedPosts() = downloadedPostDao.getAllDownloadedPosts()

    @ExperimentalCoroutinesApi
    suspend fun insertPost(post: Post) = withContext(Dispatchers.IO) {
        val postImage = urlToBitmap(context, post.url).first()
        Timber.d(postImage.toString())
        val profileImage = urlToBitmap(context, post.profileImg).first()
        Timber.d(profileImage.toString())
        val downloadedPost = DownloadedPost(
            postImage,
            profileImage,
            post.description,
            post.username,
            post.likes,
            id = post.postUid
        )
        Timber.d(downloadedPost.toString())
        downloadedPostDao.saveDownloadedPost(downloadedPost)
    }


    suspend fun deleteDownloadedPost(downloadedPost: DownloadedPost) = withContext(Dispatchers.IO) {
        downloadedPostDao.deleteDownloadedPost(downloadedPost)
    }

}
