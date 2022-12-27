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

package com.github.nkovacevic.ui.repository

import androidx.lifecycle.ViewModel
import com.github.nkovacevic.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val uiState: Flow<RepositoryUiState> = flowOf<RepositoryUiState>(RepositoryUiState.Loading)
//
//    fun addRepository(name: String) {
//        viewModelScope.launch {
//            repositoryRepository.add(name)
//        }
//    }
}

sealed interface RepositoryUiState {
    object Loading : RepositoryUiState
    data class Error(val throwable: Throwable) : RepositoryUiState
    data class Success(val data: List<String>) : RepositoryUiState
}
