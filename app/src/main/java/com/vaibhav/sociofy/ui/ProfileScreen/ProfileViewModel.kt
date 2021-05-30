package com.vaibhav.sociofy.ui.ProfileScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userId = authRepository.getCurrentUserId()
    val currentUser = MutableLiveData<User>()
    private val _profileStatus = Channel<Status>()
    val profileStatus = _profileStatus.receiveAsFlow()

    private val _userPosts = MutableLiveData<List<PostResponse>>()
    val userPosts = _userPosts

    init {
        getCurrentUser()
    }


    fun fetchUserPosts(id: String) {
        viewModelScope.launch {
            _profileStatus.send(Status.Loading)
            postRepository.getUsersPosts(
                id,
                successListener = {
                    _userPosts.postValue(it)
                    viewModelScope.launch { _profileStatus.send(Status.Success) }
                },
                failureListener = {
                    viewModelScope.launch {
                        _profileStatus.send(
                            Status.Error(
                                it.message!!
                            )
                        )
                    }
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
                viewModelScope.launch { _profileStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun dislikePost(postResponse: PostResponse) {
        viewModelScope.launch {
            postRepository.dislikePost(postResponse, successListener = {
                Timber.d("PostDisliked")
            }, failureListener = {
                viewModelScope.launch { _profileStatus.send(Status.Error(it.message!!)) }
            })
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUserDetails(successListener = {
                currentUser.postValue(it)
            }, failureListener = {})
        }
    }

    fun followUser(
        user: User
    ) {
        getCurrentUser()
        viewModelScope.launch {
            _profileStatus.send(Status.Loading)
            authRepository.followUser(currentUser.value!!, user,
                successListener = {
                    viewModelScope.launch { _profileStatus.send(Status.Success) }
                },
                failureListener = {
                    viewModelScope.launch { _profileStatus.send(Status.Error(it.message!!)) }
                })
        }

    }

    fun unFollowUser(
        user: User
    ) {
        getCurrentUser()
        viewModelScope.launch {
            _profileStatus.send(Status.Loading)
            authRepository.unFollowUser(currentUser.value!!, user,
                successListener = {
                    viewModelScope.launch { _profileStatus.send(Status.Success) }
                },
                failureListener = {
                    viewModelScope.launch { _profileStatus.send(Status.Error(it.message!!)) }
                })
        }

    }
}