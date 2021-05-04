package com.vaibhav.sociofy.data.models

import java.io.Serializable


data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profileImg: String = ""
) : Serializable {
    var bio = ""
    val following = mutableMapOf<String, String>()
    val followers = mutableMapOf<String, String>()
    val savedPosts = mutableMapOf<String, Boolean>()
    var deviceToken = ""
    fun getFollowerCount() = followers.size.toString()
    fun getFollowingCount() = following.size.toString()
}