package com.vaibhav.sociofy.ui.authorization.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.util.Constants.loginFailureMessage
import com.vaibhav.sociofy.util.Constants.loginSuccessMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    companion object {
        sealed class Events {
            object Loading : Events()
            class Success(val successMessage: String) : Events()
            class Failure(val failureMessage: String) : Events()
        }
    }

    private val _registerState = Channel<Events>()
    val registerState = _registerState.receiveAsFlow()

    fun isLoggedIn() = authRepository.isLoggedIn()

    fun registerUser(username: String, email: String, password: String) {
        if (validateFields(email, password, username)) {
            viewModelScope.launch(Dispatchers.IO) {
                _registerState.send(Events.Loading)
                authRepository.registerUser(username, email, password, successListener = {
                    viewModelScope.launch {
                        _registerState.send(Events.Success(loginSuccessMessage))
                    }
                }, { exception ->
                    viewModelScope.launch {
                        _registerState.send(Events.Failure(exception.localizedMessage ?: ""))
                    }
                })
            }
        } else {
            viewModelScope.launch {
                _registerState.send(Events.Failure(loginFailureMessage))
            }
        }
    }

    private fun validateFields(email: String, password: String, username: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "" || username.isEmpty() || username == "")
    }


}