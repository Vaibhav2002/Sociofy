package com.vaibhav.sociofy.data.models.mapper

import com.vaibhav.sociofy.data.models.local.Post
import com.vaibhav.sociofy.data.models.remote.PostResponse

class PostMapper(private val userId: String) : Mapper<PostResponse, Post> {
    override fun fromRemoteResponse(remote: PostResponse): Post {
        return Post(
            url = remote.url,
            description = remote.description,
            username = remote.username,
            profileImg = remote.profileImg,
            postUid = remote.postUid,
            userId = remote.uid,
            timeStamp = remote.timeStamp,
            likedByMe = remote.likedBy.containsKey(userId),
            likes = remote.likes
        )
    }

    override fun fromRemoteList(remoteList: List<PostResponse>): List<Post> {
        return remoteList.map {
            fromRemoteResponse(it)
        }
    }
}