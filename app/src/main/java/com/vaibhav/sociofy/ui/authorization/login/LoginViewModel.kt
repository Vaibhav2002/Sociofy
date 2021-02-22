package com.vaibhav.sociofy.ui.authorization.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.util.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    companion object {
        sealed class Events {
            object Loading : Events()
            class Success(val successMessage: String) : Events()
            class Failure(val failureMessage: String) : Events()
        }
    }

    private val _loginState = Channel<Events>()
    val loginState = _loginState.receiveAsFlow()

    fun isLoggedIn() = authRepository.isLoggedIn()

    fun loginUser(email: String, password: String) {
        if (validateFields(email, password)) {
            viewModelScope.launch {
                _loginState.send(Events.Loading)
                authRepository.loginUser(email, password, successListener = {
                    viewModelScope.launch {
                        _loginState.send(Events.Success(Constants.loginSuccessMessage))
                    }
                }, failureListener = { exception ->
                    viewModelScope.launch {
                        _loginState.send(Events.Failure(exception.localizedMessage ?: ""))
                    }
                })
            }
        } else {
            viewModelScope.launch {
                _loginState.send(Events.Failure(Constants.loginFailureMessage))
            }
        }
    }


    private fun validateFields(email: String, password: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "")
    }


}