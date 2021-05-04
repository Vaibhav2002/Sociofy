package com.vaibhav.sociofy.ui.settingScreen.downloadedPosts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vaibhav.sociofy.data.repository.PostRepository

class DownloadedPostViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel() {

    val downloadedPosts = postRepository.getDownloadedPosts()
}