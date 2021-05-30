package com.vaibhav.sociofy.data.models.remote

data class Saved(
    val userId: String = "",
    val postId: String = "",
    val timeStamp: String = System.currentTimeMillis().toString()
) {
    val saveId = userId + postId
}
