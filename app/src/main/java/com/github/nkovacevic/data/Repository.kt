package com.github.nkovacevic.data

import com.github.nkovacevic.data.remote.RemoteDataSource
import com.github.nkovacevic.data.remote.retrofit.models.RepositoryResponseModel
import com.github.nkovacevic.data.remote.retrofit.models.TagModel
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataSource: RemoteDataSource
) {
    suspend fun fetchRepositoriesByUser(user: String) = dataSource.fetchRepositoriesByUser(user)
    suspend fun fetchRepositoryDetails(
        user: String,
        repo: String
    ): Response<RepositoryResponseModel> =
        dataSource.fetchRepositoryDetails(user, repo)

    suspend fun fetchRepositoryTags(user: String, repo: String): Response<List<TagModel>> =
        dataSource.fetchRepositoryTags(user, repo)

}