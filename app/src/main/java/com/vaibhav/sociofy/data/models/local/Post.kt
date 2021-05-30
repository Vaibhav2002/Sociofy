package com.vaibhav.sociofy.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    val url: String = "",
    val description: String = "",
    val username: String = "",
    val profileImg: String = "",
    val userId: String = "",
    var timeStamp: Long = System.currentTimeMillis(),
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    @PrimaryKey
    val postUid: String = "$username+$timeStamp"
)
