package com.vaibhav.sociofy.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.sociofy.data.models.Notification
import com.vaibhav.sociofy.data.models.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {


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
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            getProfileImageUrl(successListener = { url ->
                getPostImageUri(filename, successListener = { posturl ->
                    val post = Post(
                        posturl,
                        description,
                        auth.currentUser!!.displayName!!,
                        url,
                        auth.currentUser!!.uid,
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
            fireStore.collection("notifications").addSnapshotListener { value, error ->
                val list = mutableListOf<Notification>()
                if (error != null)
                    failureListener.invoke(error)
                else {
                    value?.let {
                        for (not in value.documents) {
                            val notification = not.toObject<Notification>()
                            if (notification!!.uid != auth.currentUser!!.uid)
                                list.add(notification)
                        }
                    }
                    list.sortByDescending { it.timestamp }
                    successListener.invoke(list)
                }
            }
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

}
