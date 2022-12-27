/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nkovacevic.data.di

import android.app.Application
import com.github.nkovacevic.data.remote.RemoteDataSource
import com.github.nkovacevic.data.remote.RemoteDataSourceImpl
import com.github.nkovacevic.data.remote.retrofit.RepositoryEndpoint
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        application: Application
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }

    @Provides
    @Singleton
    fun bindEndpoint(retrofit: Retrofit): RepositoryEndpoint {
        return retrofit.create(RepositoryEndpoint::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(endpoint: RepositoryEndpoint): RemoteDataSource {
        return RemoteDataSourceImpl(endpoint)
    }

}

