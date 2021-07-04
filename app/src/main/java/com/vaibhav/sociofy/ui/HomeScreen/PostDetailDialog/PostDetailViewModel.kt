package com.vaibhav.sociofy.ui.HomeScreen.PostDetailDialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PostDetailViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel() {


    private val _postDetailStatus = Channel<Status>()
    val postDetailStatus = _postDetailStatus.receiveAsFlow()

    private val _post = MutableLiveData<Post>()
    val post = _post

    fun getPostFromId(id: String) {
        viewModelScope.launch {
            _postDetailStatus.send(Status.Loading)
            postRepository.getPostFromId(id, successListener = {
                _post.postValue(it)
            },
                failureListener = { error ->
                    viewModelScope.launch { _postDetailStatus.send(Status.Error(error.message!!)) }
                }
            )

        }
    }


}