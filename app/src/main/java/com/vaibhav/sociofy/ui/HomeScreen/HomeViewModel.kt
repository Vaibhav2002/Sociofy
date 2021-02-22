package com.vaibhav.sociofy.ui.HomeScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.Notification
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    val userId = authRepository.getCurrentUserId()

    private val _feed = MutableLiveData<List<Post>>()
    val feed = _feed

    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts = _userPosts

    private val _userDetails = MutableLiveData<User>()
    val userDetails = _userDetails

    private val _usersList = MutableLiveData<List<User>>()
    val userList = _usersList

    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList = _notificationList


    private val _feedStatus = Channel<Status>()
    val feedStatus = _feedStatus.receiveAsFlow()

    private val _userPageStatus = Channel<Status>()
    val userPageStatus = _userPageStatus.receiveAsFlow()

    private val _userListStatus = Channel<Status>()
    val userListStatus = _userListStatus.receiveAsFlow()

    private val _notificationStatus = Channel<Status>()
    val notificationStatus = _notificationStatus.receiveAsFlow()


    init {
        getUserDetails()
        getPosts()
        getUserPosts()
        getUserList()
        getNotifications()
    }

    private fun getPosts() {
        viewModelScope.launch {
            Timber.d("Called")
            _feedStatus.send(Status.Loading)
            postRepository.getAllFeedPosts(successListener = { posts ->
                posts.sortByDescending { it.timeStamp }
                _feed.postValue(posts)
                viewModelScope.launch { _feedStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message.toString())) }
            })
        }
    }


    private fun getUserDetails() {
        viewModelScope.launch {
            _userPageStatus.send(Status.Loading)
            authRepository.getCurrentUserDetails(successListener = {
                _userDetails.postValue(it)
                Timber.d(it.toString())
                viewModelScope.launch { _userPageStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _userPageStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun getUserPosts() {
        viewModelScope.launch {
            _userPageStatus.send(Status.Loading)
            postRepository.getUsersPosts(
                authRepository.getCurrentUserId(),
                successListener = {
                    _userPosts.postValue(it)
                    viewModelScope.launch { _userPageStatus.send(Status.Success) }
                },
                failureListener = {
                    viewModelScope.launch { _userPageStatus.send(Status.Error(it.message!!)) }
                })
        }
    }

    private fun getUserList() {
        viewModelScope.launch {
            _userListStatus.send(Status.Loading)
            authRepository.getAllUsers(successListener = {
                _usersList.postValue(it)
                Timber.d(it.toString())
                viewModelScope.launch { _userListStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _userListStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun getNotifications() {
        viewModelScope.launch {
            _notificationStatus.send(Status.Loading)
            postRepository.getNotifications(successListener = { notifications ->
                _notificationList.postValue(notifications)
                viewModelScope.launch { _notificationStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _notificationStatus.send(Status.Error(it.message!!)) }
            })
        }

    }

    fun likeButtonPressed(post: Post) = likePost(post)
    fun dislikeButtonPressed(post: Post) = dislikePost(post)

    private fun likePost(post: Post) {
        viewModelScope.launch {
            postRepository.likePost(post, successListener = {
                Timber.d("PostLiked")
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun dislikePost(post: Post) {
        viewModelScope.launch {
            postRepository.dislikePost(post, successListener = {
                Timber.d("PostDisliked")
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message!!)) }
            })
        }
    }
}