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

package com.github.nkovacevic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nkovacevic.data.Repository
import com.github.nkovacevic.data.remote.retrofit.models.RepositoryResponseModel
import com.github.nkovacevic.data.remote.retrofit.models.TagModel
import com.github.nkovacevic.data.remote.retrofit.models.UserModel
import com.github.nkovacevic.presentation.models.DetailsData
import com.github.nkovacevic.presentation.models.RepositoryOwner
import com.github.nkovacevic.presentation.models.Tag
import com.github.nkovacevic.presentation.models.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _state: MutableStateFlow<RepositoriesUiState> =
        MutableStateFlow(RepositoriesUiState.Loading)
    val uiState: Flow<RepositoriesUiState>
        get() = _state

    private val _detailsState: MutableStateFlow<DetailsUiState> =
        MutableStateFlow(DetailsUiState.Loading)
    val detailsUiState: Flow<DetailsUiState>
        get() = _detailsState


    fun fetchRepositoriesByUser(user: String = DEFAULT_USER) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.fetchRepositoriesByUser(user)
            if (res.isSuccessful)
                res.body()
                    ?.map { it.toUIModel() }
                    ?.let {
                        _state.value = RepositoriesUiState.Success(it)
                    }
            else {
                _state.value = RepositoriesUiState.Error(res.code().toUiString())
            }

        }
    }


    fun fetchDetailsData(userName: String, repoName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailsResponse = repository.fetchRepositoryDetails(
                userName,
                repoName
            )
            val tagResponse = repository.fetchRepositoryTags(
                userName,
                repoName
            )
            var state: DetailsUiState.Success? = null
            if (detailsResponse.isSuccessful) {
                detailsResponse.body()?.let { body ->
                    state = DetailsUiState.Success(
                        selected = DetailsData(
                            body.toUIModel()
                        )
                    )
                }
            }
            if (tagResponse.isSuccessful) {
                tagResponse.body()?.let { body ->
                    state?.let { // Can't display state w/o detailed response
                        state =
                            it.copy(
                                selected = DetailsData(
                                    selectedRepository = it.selected.selectedRepository,
                                    tags = body.map { it.toUIModel() }
                                )
                            )
                    }
                }
            }
            if (tagResponse.isSuccessful && detailsResponse.isSuccessful) {
                state?.let { success ->
                    _detailsState.value = success
                }
            } else {
                _detailsState.value = DetailsUiState.Error(tagResponse.code().toUiString())
            }


        }
    }


    companion object {
        const val DEFAULT_USER: String = "octocat"
    }
}

private fun Int.toUiString(): String {
    return when (this) {
        403 -> "Forbidden!"
        404 -> "Not found!"
        418 -> "Server is a teapot!"
        500 -> "Something went wrong on the server!"
        else -> "Something went wrong"
    }
}

private fun RepositoryResponseModel.toUIModel(): UserRepository =
    UserRepository(
        id = id,
        name = name,
        watchers = watchersCount,
        forks = forksCount,
        numberOfIssues = openIssues,
        repositoryOwner = owner.toUIModel()
    )

private fun UserModel.toUIModel(): RepositoryOwner =
    RepositoryOwner(
        avatar = avatarUrl,
        name = login
    )

private fun TagModel.toUIModel(): Tag =
    Tag(
        name = name,
        sha = commit.sha
    )

sealed interface RepositoriesUiState {
    object Loading : RepositoriesUiState
    data class Error(val uiError: String) : RepositoriesUiState
    data class Success(val data: List<UserRepository>) : RepositoriesUiState
}

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Error(val uiError: String) : DetailsUiState
    data class Success(val selected: DetailsData) : DetailsUiState
}