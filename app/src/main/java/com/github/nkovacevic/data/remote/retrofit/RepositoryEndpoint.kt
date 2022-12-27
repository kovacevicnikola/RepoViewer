package com.github.nkovacevic.data.remote.retrofit

import com.github.nkovacevic.data.remote.retrofit.models.RepositoryModel
import retrofit2.Response
import retrofit2.http.GET

interface RepositoryEndpoint {
    @GET
    suspend fun getRepositories(): Response<List<RepositoryModel>>
}