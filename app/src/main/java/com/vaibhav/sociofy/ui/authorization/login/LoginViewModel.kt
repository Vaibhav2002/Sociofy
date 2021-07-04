package com.vaibhav.sociofy.ui.authorization.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.data.repo.AuthRepo
import com.vaibhav.sociofy.data.repository.Preferences
import com.vaibhav.sociofy.util.Shared.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val authRepo: AuthRepo,
    private val preferences: Preferences
) :
    ViewModel() {


    private val _loginState = MutableLiveData<Resource<User>>()
    val loginState: LiveData<Resource<User>> = _loginState

    fun isLoggedIn() = authRepo.isUserLoggedIn()


    fun loginUser(email: String, password: String) = viewModelScope.launch {
        if (validateFields(email, password)) {
            authRepo.loginUser(email, password).collect {
                _loginState.postValue(it)
            }
        } else
            _loginState.postValue(Resource.Error("Enter all fields correctly"))
    }

    private fun validateFields(email: String, password: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "")
    }


}