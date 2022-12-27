package com.github.nkovacevic.data.remote

import com.github.nkovacevic.data.remote.retrofit.RepositoryEndpoint
import com.github.nkovacevic.data.remote.retrofit.models.RepositoryModel
import retrofit2.Response
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(
    private val repositoryEndpoint: RepositoryEndpoint
) : RemoteDataSource {
    override suspend fun fetchData(): Response<List<RepositoryModel>> =
        repositoryEndpoint.getRepositories()

}