package com.github.nkovacevic.data.remote.retrofit.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserModel(
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val login: String
)