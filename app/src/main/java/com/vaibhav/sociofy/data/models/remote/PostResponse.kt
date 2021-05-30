package com.vaibhav.sociofy.data.models.remote

import java.io.Serializable


data class PostResponse(
    val url: String = "",
    val description: String = "",
    val username: String = "",
    val profileImg: String = "",
    val uid: String = "",
    var timeStamp: Long = System.currentTimeMillis(),
    var likes: Int = 0,
    val likedBy: MutableMap<String, Boolean> = mutableMapOf(),
    val postUid: String = "$username+$timeStamp"
) : Serializable {


}