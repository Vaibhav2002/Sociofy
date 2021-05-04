package com.vaibhav.sociofy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vaibhav.sociofy.data.models.local.DownloadedPost

@Dao
interface PostDao {

    @Query("SELECT * FROM post_table ORDER BY timeStamp DESC")
    fun getAllDownloadedPosts(): LiveData<List<DownloadedPost>>

    @Insert
    suspend fun saveDownloadedPost(downloadedPost: DownloadedPost)

    @Delete
    suspend fun deleteDownloadedPost(downloadedPost: DownloadedPost)

    @Query("DELETE FROM post_table")
    suspend fun deleteAllDownloadedPost()

}