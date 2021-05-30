package com.vaibhav.sociofy.ui.settingScreen.savedPosts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Constants.DEFAULT_ERROR
import com.vaibhav.sociofy.util.Shared.Resource
import kotlinx.coroutines.launch
import timber.log.Timber


class SavedPostsViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _savedPosts = MutableLiveData<Resource<List<PostResponse>>>()
    val savedPostResponse: LiveData<Resource<List<PostResponse>>> = _savedPosts

    val userId = authRepository.getCurrentUserId()

    init {
        getSavedPosts()
    }

    fun getSavedPosts() = viewModelScope.launch {
        _savedPosts.postValue(Resource.Loading())
        postRepository.getSavedPosts(userId,
            onSuccessListener = { savedposts ->
                val posts = mutableListOf<PostResponse>()
                for (p in savedposts) {
                    posts.add(p.postResponse.copy(timeStamp = p.timeStamp.toLong()))
                }
                posts.sortBy { it.timeStamp }
                _savedPosts.postValue(Resource.Success(posts))
            },
            onFailureListener = {
                _savedPosts.postValue(Resource.Error(it.localizedMessage ?: DEFAULT_ERROR))
            })
    }

    fun likeButtonPressed(postResponse: PostResponse) = likePost(postResponse)
    fun dislikeButtonPressed(postResponse: PostResponse) = dislikePost(postResponse)

    private fun likePost(postResponse: PostResponse) {
        viewModelScope.launch {
            postRepository.likePost(postResponse, successListener = {
                Timber.d("PostLiked")
            }, failureListener = {
                _savedPosts.postValue(Resource.Error("Failed to like post"))
            })
        }
    }

    private fun dislikePost(postResponse: PostResponse) {
        viewModelScope.launch {
            postRepository.dislikePost(postResponse, successListener = {
                Timber.d("PostDisliked")
            }, failureListener = {
                _savedPosts.postValue(Resource.Error("Failed to dislike post"))
            })
        }
    }


//    fun deleteSavedPost(saved: Saved) = viewModelScope.launch {
//        _savedPosts.postValue(Resource.Loading())
//        postRepository.removeSavedPost(saved.saveId,
//            successListener = {
//                getSavedPosts()
//            },
//            failureListener = {
//                _savedPosts.postValue(Resource.Error(it.localizedMessage ?: DEFAULT_ERROR))
//            }
//        )
//    }
}