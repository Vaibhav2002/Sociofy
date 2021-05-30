package com.vaibhav.sociofy.ui.UploadScreen

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.PostRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class UploadViewModel @ViewModelInject constructor(
    private val repository: PostRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {


    private val original_imageUri =
        Uri.parse("android.resource://com.vaibhav.sociofy/drawable/image")
    var uri: Uri? = null
    private val _uploadStatus = Channel<Status>()
    val uploadStatus = _uploadStatus.receiveAsFlow()

    private val _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User> = _userDetails

    init {
        getUserData()
    }


    private fun getUserData() = viewModelScope.launch {
        authRepository.getCurrentUserDetails({
            _userDetails.postValue(it)
        }, {

        })
    }

    fun uploadImage(description: String) {
        uri?.let {
            if (validate(uri!!, description)) {
                Timber.d("InViewModel")
                viewModelScope.launch {
                    _uploadStatus.send(Status.Loading)
                    repository.uploadInStorage(uri!!, successListener = { filename ->
                        uploadPost(filename, description)
                        sendNotification(description)
                    },
                        failureListener = {
                            viewModelScope.launch {
                                _uploadStatus.send(Status.Error(it.message!!))
                            }
                        })
                }
            }
        } ?: viewModelScope.launch {
            _uploadStatus.send(Status.Error("Invalid input"))
        }

    }


    private fun sendNotification(description: String) = viewModelScope.launch {
        userDetails.value?.let {
            Timber.d("Sending $description")
            repository.sendPushNotification(description, it)
        }
    }

    private fun uploadPost(filename: String, description: String) {
        viewModelScope.launch {
            _uploadStatus.send(Status.Loading)
            repository.uploadPost(
                user = _userDetails.value!!,
                filename = filename,
                description = description,
                successListener = {
                    viewModelScope.launch {
                        _uploadStatus.send(Status.Success)
                    }
                },
                failureListener = {
                    viewModelScope.launch {
                        _uploadStatus.send(Status.Error(it.message!!))
                    }
                })
        }

    }


    private fun validate(uri: Uri, description: String) =
        !(uri == original_imageUri || description.isEmpty() || description == "")


}