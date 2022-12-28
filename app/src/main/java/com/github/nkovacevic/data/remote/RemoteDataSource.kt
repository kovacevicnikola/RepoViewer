package com.github.nkovacevic.data.remote

import com.github.nkovacevic.data.remote.retrofit.models.RepositoryResponseModel
import com.github.nkovacevic.data.remote.retrofit.models.TagModel
import retrofit2.Response

interface RemoteDataSource {
    suspend fun fetchRepositoriesByUser(user: String): Response<List<RepositoryResponseModel>>
    suspend fun fetchRepositoryDetails(
        user: String,
        repo: String
    ): Response<RepositoryResponseModel>

    suspend fun fetchRepositoryTags(user: String, repo: String): Response<List<TagModel>>
}