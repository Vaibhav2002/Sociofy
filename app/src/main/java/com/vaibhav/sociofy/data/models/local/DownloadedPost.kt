package com.vaibhav.sociofy.data.models.local

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class DownloadedPost(
    val url: Bitmap,
    val profileImg: Bitmap,
    val description: String,
    val username: String,
    var likes: Int = 0,
    var timeStamp: Long = System.currentTimeMillis(),
    @PrimaryKey
    var id: String = "",
)