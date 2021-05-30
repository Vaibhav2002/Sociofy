package com.vaibhav.sociofy.data.local.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vaibhav.sociofy.data.models.local.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post_table ORDER BY timeStamp DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM post_table WHERE userId IN (:following) ORDER BY timeStamp DESC")
    fun getAllFeedPosts(following: List<String>): Flow<List<Post>>

    @Query("SELECT * FROM post_table WHERE userId = :userId ORDER BY timeStamp DESC")
    fun getMyPosts(userId: String): Flow<List<Post>>

    @Query("SELECT * FROM post_table WHERE userId = :userId ORDER BY timeStamp DESC")
    fun getPostsOfUser(userId: String): List<Post>

    @Insert
    fun insertPost(post: Post)


}