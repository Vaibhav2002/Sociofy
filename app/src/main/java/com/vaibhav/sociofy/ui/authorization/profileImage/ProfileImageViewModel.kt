package com.vaibhav.sociofy.ui.authorization.profileImage

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.repo.AuthRepo
import com.vaibhav.sociofy.util.Shared.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileImageViewModel @ViewModelInject constructor(private val authRepo: AuthRepo) :
    ViewModel() {

    var uri = Uri.parse("android.resource://com.vaibhav.sociofy/drawable/profile")
    private val _uploadStatus = MutableLiveData<Resource<Unit>>()
    val uploadStatus: LiveData<Resource<Unit>> = _uploadStatus

    private val userData = authRepo.getUserDetails()

    fun upload() = viewModelScope.launch {
        authRepo.updateUser(uri, userData.username, userData.bio).collect {
            _uploadStatus.postValue(it)
        }
    }

}