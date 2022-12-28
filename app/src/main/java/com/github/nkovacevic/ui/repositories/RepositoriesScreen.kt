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

package com.github.nkovacevic.ui.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.github.nkovacevic.presentation.RepositoriesUiState
import com.github.nkovacevic.presentation.RepositoryViewModel
import com.github.nkovacevic.presentation.models.UserRepository
import java.text.NumberFormat

@Composable
fun RepositoryScreen(
    modifier: Modifier = Modifier,
    onItemClicked: (repository: UserRepository) -> Unit,
    viewModel: RepositoryViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<RepositoriesUiState>(
        initialValue = RepositoriesUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LaunchedEffect(key1 = null) {
        viewModel.fetchRepositoriesByUser() // Load octocat for convenience
    }
    RepositoryScreen(
        state = state,
        onSearchClicked = viewModel::fetchRepositoriesByUser,
        onItemClicked = onItemClicked,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RepositoryScreen(
    state: RepositoriesUiState,
    onSearchClicked: (name: String) -> Unit,
    onItemClicked: (repository: UserRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var searchBarValue by remember { mutableStateOf(RepositoryViewModel.DEFAULT_USER) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = searchBarValue,
                onValueChange = { searchBarValue = it },
                modifier = Modifier.weight(1f),
                enabled = state !is RepositoriesUiState.Loading
            )

            Button(
                onClick = { onSearchClicked(searchBarValue) },
                modifier = Modifier.fillMaxHeight()
            ) {
                Text("Search")
            }
        }
        when (state) {
            is RepositoriesUiState.Success -> {
                RepositoryList(state.data, onItemClicked)
            }
            is RepositoriesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.uiError,
                        style = MaterialTheme.typography.titleMedium
                    )

                }
            }
            is RepositoriesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()

                }
            }
        }
    }
}

@Composable
internal fun RepositoryList(
    items: List<UserRepository>,
    onItemClicked: (repository: UserRepository) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        itemsIndexed(items, key = { index, item -> item.id + index }) { _, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClicked(item)
                    },
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        NumberFormat.getInstance().format(item.numberOfIssues),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

        }
    }
}