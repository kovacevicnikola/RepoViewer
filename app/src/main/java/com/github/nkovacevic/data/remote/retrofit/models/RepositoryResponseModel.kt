package com.github.nkovacevic.data.remote.retrofit.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryResponseModel(
    val id: Long,
    @SerialName("open_issues")
    val openIssues: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("watchers")
    val watchersCount: Int,
    val name: String,
    val owner: UserModel
)