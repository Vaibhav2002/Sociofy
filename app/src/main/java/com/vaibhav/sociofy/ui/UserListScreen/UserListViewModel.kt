package com.vaibhav.sociofy.ui.UserListScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.util.Shared.Status
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class UserListViewModel @ViewModelInject constructor(private val repo: AuthRepository) :
    ViewModel() {

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    private val _userListStatus = Channel<Status>()
    val userListStatus = _userListStatus.receiveAsFlow()

    fun getUsersList(users: MutableMap<String, String>) {
        viewModelScope.launch {
            _userListStatus.send(Status.Loading)
            repo.getUsersByFilter(users, successListener = {
                _userList.postValue(it)
                viewModelScope.launch { _userListStatus.send(Status.Success) }
            }, {
                viewModelScope.launch { _userListStatus.send(Status.Error(it.message ?: "")) }
            })
        }
    }


}