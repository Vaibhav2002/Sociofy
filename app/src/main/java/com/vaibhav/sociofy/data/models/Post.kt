package com.vaibhav.sociofy.data.models

import java.io.Serializable


data class Post(
    val url: String = "",
    val description: String = "",
    val username: String = "",
    val profileImg: String = "",
    val uid: String = "",
    val timeStamp: Long = System.currentTimeMillis()
) : Serializable {
    var likes = 0
    val likedBy = mutableMapOf<String, Boolean>()
    val postUid = "$username+$timeStamp"


}