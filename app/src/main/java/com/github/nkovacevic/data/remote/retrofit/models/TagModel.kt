package com.github.nkovacevic.data.remote.retrofit.models

@kotlinx.serialization.Serializable
data class TagModel(
    val name: String,
    val commit: CommitModel
)

@kotlinx.serialization.Serializable
data class CommitModel(
    val sha: String,
    val url: String
)