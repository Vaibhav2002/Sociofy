package com.vaibhav.sociofy.util.Shared

sealed class Status {
    object Loading : Status()
    object Success : Status()
    class Error(val error: String) : Status()
}