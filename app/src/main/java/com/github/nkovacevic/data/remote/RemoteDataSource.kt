package com.github.nkovacevic.data.remote

import com.github.nkovacevic.data.remote.retrofit.models.RepositoryModel
import retrofit2.Response

interface RemoteDataSource {
    suspend fun fetchData(): Response<List<RepositoryModel>>
}