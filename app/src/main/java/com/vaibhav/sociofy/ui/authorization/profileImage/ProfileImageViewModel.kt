package com.vaibhav.sociofy.ui.authorization.profileImage

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileImageViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    companion object {
        sealed class UploadStatus() {
            object Loading : UploadStatus()
            object Success : UploadStatus()
            class Error(val error: String) : UploadStatus()
        }
    }

    var uri = Uri.parse("android.resource://com.vaibhav.sociofy/drawable/profile")
    private val _uploadStatus = Channel<UploadStatus>()
    val uploadStatus = _uploadStatus.receiveAsFlow()

    fun upload() {
        viewModelScope.launch {
            _uploadStatus.send(UploadStatus.Loading)
            authRepository.setUserImage(uri!!,
                successListener = {
                    viewModelScope.launch { _uploadStatus.send(UploadStatus.Success) }
                },
                failureListener = {
                    viewModelScope.launch { _uploadStatus.send(UploadStatus.Error(it.message!!)) }
                })
        }
    }

}