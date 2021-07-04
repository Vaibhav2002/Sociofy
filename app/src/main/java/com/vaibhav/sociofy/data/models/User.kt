package com.vaibhav.sociofy.data.models

import java.io.Serializable


data class User(
    val id: String = "",
    var username: String = "",
    val email: String = "",
    var profileImg: String = ""
) : Serializable {
    var bio = ""
    val following = mutableMapOf<String, String>()
    val followers = mutableMapOf<String, String>()
    val savedPosts = mutableMapOf<String, Boolean>()
    var deviceToken = ""
    fun getFollowerCount() = followers.size.toString()
    fun getFollowingCount() = following.size.toString()
}