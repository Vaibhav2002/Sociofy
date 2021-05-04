package com.vaibhav.sociofy.data.models

import java.io.Serializable


data class Post(
    val url: String = "",
    val description: String = "",
    val username: String = "",
    val profileImg: String = "",
    val uid: String = "",
    var timeStamp: Long = System.currentTimeMillis(),
    var likes: Int = 0,
    val likedBy: MutableMap<String, Boolean> = mutableMapOf<String, Boolean>(),
    val postUid: String = "$username+$timeStamp"
) : Serializable {


}