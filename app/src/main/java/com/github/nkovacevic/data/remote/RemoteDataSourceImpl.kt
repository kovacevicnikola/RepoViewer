package com.github.nkovacevic.data.remote

import com.github.nkovacevic.data.remote.retrofit.RepositoryEndpoint
import com.github.nkovacevic.data.remote.retrofit.models.RepositoryResponseModel
import com.github.nkovacevic.data.remote.retrofit.models.TagModel
import retrofit2.Response
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(
    private val repositoryEndpoint: RepositoryEndpoint
) : RemoteDataSource {
    override suspend fun fetchRepositoriesByUser(user: String): Response<List<RepositoryResponseModel>> =
        repositoryEndpoint.getRepositories(user)

    override suspend fun fetchRepositoryDetails(
        user: String,
        repo: String
    ): Response<RepositoryResponseModel> =
        repositoryEndpoint.getRepositoryDetails(user, repo)

    override suspend fun fetchRepositoryTags(user: String, repo: String): Response<List<TagModel>> =
        repositoryEndpoint.getRepositoryTags(user, repo)

}