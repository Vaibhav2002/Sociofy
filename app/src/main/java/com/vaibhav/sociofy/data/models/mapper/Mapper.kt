package com.vaibhav.sociofy.data.models.mapper

interface Mapper<Remote, Local> {

    fun fromRemoteResponse(remote: Remote): Local

    fun fromRemoteList(remoteList: List<Remote>): List<Local>
}