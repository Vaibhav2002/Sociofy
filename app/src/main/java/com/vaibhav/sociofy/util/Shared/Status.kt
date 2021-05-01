package com.vaibhav.sociofy.util.Shared

sealed class Status {
    object Loading : Status()
    object Success : Status()
    class Error(val error: String) : Status()
}

sealed class InteractionStatus {
    object Loading : InteractionStatus()
    class Success(val message: String) : InteractionStatus()
    class Error(val error: String) : InteractionStatus()
}