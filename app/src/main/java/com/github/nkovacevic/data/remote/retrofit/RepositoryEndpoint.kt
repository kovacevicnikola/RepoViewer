package com.github.nkovacevic.data.remote.retrofit

import com.github.nkovacevic.data.remote.retrofit.models.RepositoryResponseModel
import com.github.nkovacevic.data.remote.retrofit.models.TagModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryEndpoint {
    @GET("users/{user}/repos")
    suspend fun getRepositories(@Path("user") user: String): Response<List<RepositoryResponseModel>>

    @GET("repos/{user}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("user") userName: String,
        @Path("repo") repositoryName: String
    ): Response<RepositoryResponseModel>

    @GET("repos/{user}/{repo}/tags")
    suspend fun getRepositoryTags(
        @Path("user") userName: String,
        @Path("repo") repositoryName: String
    ): Response<List<TagModel>>

}