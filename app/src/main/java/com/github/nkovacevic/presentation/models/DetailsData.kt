package com.github.nkovacevic.presentation.models

data class DetailsData(
    val selectedRepository: UserRepository,
    val tags: List<Tag> = emptyList()
)