package com.vaibhav.sociofy.ui.authorization.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repo.AuthRepo
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.util.Shared.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val authRepo: AuthRepo
) :
    ViewModel() {

    private val _registerState = MutableLiveData<Resource<User>>()
    val registerState: LiveData<Resource<User>> = _registerState

    fun isLoggedIn() = authRepository.isLoggedIn()

    fun registerUser(username: String, email: String, password: String) =
        viewModelScope.launch {
            if (validateFields(email, password, username))
                authRepo.registerUser(email, username, password).collect { status ->
                    _registerState.postValue(status)
                }
            else
                _registerState.postValue(Resource.Error("Enter all fields correctly"))
        }


    private fun validateFields(email: String, password: String, username: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "" || username.isEmpty() || username == "")
    }


}