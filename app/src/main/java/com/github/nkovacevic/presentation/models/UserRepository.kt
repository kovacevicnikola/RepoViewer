package com.github.nkovacevic.presentation.models

data class UserRepository(
    val id: Long,
    val name: String,
    val numberOfIssues: Int,
    val watchers: Int = 0,
    val forks: Int = 0,
    val repositoryOwner: RepositoryOwner
)