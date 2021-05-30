package com.vaibhav.sociofy.ui.HomeScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.Notification
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Shared.InteractionStatus
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    val userId = authRepository.getCurrentUserId()

    //data

    private val _allFeed = MutableLiveData<List<PostResponse>>()
    val allFeed = _allFeed

    private val _feed = MutableLiveData<List<PostResponse>>()
    val feed = _feed

    private val _userPosts = MutableLiveData<List<PostResponse>>()
    val userPosts = _userPosts

    private val _userDetails = MutableLiveData<User>()
    val userDetails = _userDetails

    private val _usersList = MutableLiveData<List<User>>()
    val userList = _usersList

    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList = _notificationList


    //status

    private val _feedStatus = Channel<Status>()
    val feedStatus = _feedStatus.receiveAsFlow()

    private val _userPageStatus = Channel<Status>()
    val userPageStatus = _userPageStatus.receiveAsFlow()

    private val _searchScreenStatus = Channel<Status>()
    val searchScreenStatus = _searchScreenStatus.receiveAsFlow()

    private val _notificationStatus = Channel<Status>()
    val notificationStatus = _notificationStatus.receiveAsFlow()

    private val _postInteractionStatus = MutableLiveData<InteractionStatus>()
    val postInteractionStatus: LiveData<InteractionStatus> = _postInteractionStatus

    init {
        getUserDetails()
        getPosts()
        getAllPosts()
        getUserPosts()
        getUserList()
        getNotifications()


    }

    private fun getPosts() {
        viewModelScope.launch {
            Timber.d("getPosts Called")
            Timber.d(_userDetails.value.toString())
            _feedStatus.send(Status.Loading)
            postRepository.getFeedOfFollowers(successListener = { posts ->
                posts.sortByDescending { it.timeStamp }
                _feed.postValue(posts)
                viewModelScope.launch { _feedStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message.toString())) }
            })
        }
    }

    private fun getAllPosts() {
        viewModelScope.launch {
            Timber.d("getPosts Called")
            Timber.d(_userDetails.value.toString())
            _searchScreenStatus.send(Status.Loading)
            postRepository.getAllFeedPosts(successListener = { posts ->
                posts.sortByDescending { it.timeStamp }
                _allFeed.postValue(posts)
                viewModelScope.launch { _searchScreenStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _searchScreenStatus.send(Status.Error(it.message.toString())) }
            })
        }
    }


    private fun getUserDetails() {

        viewModelScope.launch {
            Timber.d("getUserDetails Called")
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
            Timber.d("getUserPosts Called")
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
            Timber.d("getUSerList Called")
            _searchScreenStatus.send(Status.Loading)
            authRepository.getAllUsers(successListener = {
                _usersList.postValue(it)
                viewModelScope.launch { _searchScreenStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _searchScreenStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun getNotifications() {
        viewModelScope.launch {
            Timber.d("getNotifications Called")
            _notificationStatus.send(Status.Loading)
            postRepository.getNotifications(successListener = { notifications ->
                _notificationList.postValue(notifications)
                viewModelScope.launch { _notificationStatus.send(Status.Success) }
            }, failureListener = {
                viewModelScope.launch { _notificationStatus.send(Status.Error(it.message!!)) }
            })
        }

    }

    fun likeButtonPressed(postResponse: PostResponse) = likePost(postResponse)
    fun dislikeButtonPressed(postResponse: PostResponse) = dislikePost(postResponse)

    private fun likePost(postResponse: PostResponse) {
        viewModelScope.launch {
            postRepository.likePost(postResponse, successListener = {
                Timber.d("PostLiked")
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun dislikePost(postResponse: PostResponse) {
        viewModelScope.launch {
            postRepository.dislikePost(postResponse, successListener = {
                Timber.d("PostDisliked")
            }, failureListener = {
                viewModelScope.launch { _feedStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    fun savePost(postResponse: PostResponse) =
        viewModelScope.launch {
            postRepository.savePost(userId = userId, postId = postResponse.postUid,
                successListener = {
                    _postInteractionStatus.postValue(InteractionStatus.Success("Post Saved successfully"))
                    _postInteractionStatus.postValue(InteractionStatus.Loading)
                },
                failureListener = {
                    _postInteractionStatus.postValue(InteractionStatus.Success("Post Save unsuccessful"))

                }
            )
        }

    fun onDownloadPostPressed(postResponse: PostResponse) = downloadPost(postResponse)

    @ExperimentalCoroutinesApi
    private fun downloadPost(postResponse: PostResponse) = viewModelScope.launch {
        postRepository.insertPost(postResponse)
    }
}
