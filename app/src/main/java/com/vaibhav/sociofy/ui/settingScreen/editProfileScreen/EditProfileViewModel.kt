package com.vaibhav.sociofy.ui.settingScreen.editProfileScreen

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditProfileViewModel @ViewModelInject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {


    private val _editProfileStatus = Channel<Status>()
    val editProfileStatus = _editProfileStatus.receiveAsFlow()

    fun updateUser(image: Uri?, username: String, bio: String) {
        viewModelScope.launch {
            _editProfileStatus.send(Status.Loading)
            try {
                authRepo.updateUser(
                    username,
                    bio,
                    image,
                    successListener = {
                        viewModelScope.launch { _editProfileStatus.send(Status.Success) }
                    },
                    failureListener = {
                        viewModelScope.launch {
                            _editProfileStatus.send(
                                Status.Error(it.localizedMessage ?: "Oops something went wrong")
                            )
                        }
                    })
            } catch (exception: Exception) {
                _editProfileStatus.send(
                    Status.Error(
                        exception.localizedMessage ?: "Oops somehting went wrong"
                    )
                )
            }
        }
    }

}