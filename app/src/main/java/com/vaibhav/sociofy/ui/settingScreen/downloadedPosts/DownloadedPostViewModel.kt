package com.vaibhav.sociofy.ui.settingScreen.downloadedPosts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.local.DownloadedPost
import com.vaibhav.sociofy.data.repository.PostRepository
import kotlinx.coroutines.launch

class DownloadedPostViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel() {

    val downloadedPosts = postRepository.getDownloadedPosts()

    fun deleteDownloadedPost(downloadedPost: DownloadedPost) = viewModelScope.launch {
        postRepository.deleteDownloadedPost(downloadedPost)
    }
}