package com.vaibhav.sociofy.ui.settingScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.data.repository.AuthRepository


class SettingsViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _userDetail = MutableLiveData<User>()
    val userDetail: LiveData<User> = _userDetail

    fun setUser(user: User) {
        _userDetail.postValue(user)
    }

    fun logoutUser() = authRepository.logOut()

}