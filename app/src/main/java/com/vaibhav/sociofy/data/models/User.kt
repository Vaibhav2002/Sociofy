package com.vaibhav.sociofy.data.models

import java.io.Serializable


data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profileImg: String = ""
) : Serializable {
    val following = mutableMapOf<String, Boolean>()
    val followers = mutableMapOf<String, Boolean>()
    fun getFollowerCount() = followers.size.toString()
    fun getFollowingCount() = following.size.toString()
}