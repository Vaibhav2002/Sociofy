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


sealed class Resource<T>(
    val data: T? = null,
    val message: String = ""
) {
    class Loading<T>() : Resource<T>()
    class Success<T>(data: T? = null, message: String = "") : Resource<T>(data, message)
    class Error<T>(message: String = "Oops something went wrong") : Resource<T>(message = message)
}