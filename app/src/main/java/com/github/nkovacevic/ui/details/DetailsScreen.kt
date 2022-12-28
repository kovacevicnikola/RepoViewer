package com.github.nkovacevic.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.github.nkovacevic.presentation.DetailsUiState
import com.github.nkovacevic.presentation.RepositoryViewModel
import com.github.nkovacevic.presentation.models.RepositoryOwner

@Composable
fun DetailsScreen(
    viewModel: RepositoryViewModel = hiltViewModel(),
    userName: String,
    repoName: String
) {
    viewModel.fetchDetailsData(userName, repoName)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<DetailsUiState>(
        initialValue = DetailsUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.detailsUiState.collect { value = it }
        }
    }
    DetailsScreen(state = state)
}

@Composable
internal fun DetailsScreen(
    state: DetailsUiState
) {
    when (state) {
        is DetailsUiState.Error -> {
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
        is DetailsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()

            }
        }
        is DetailsUiState.Success -> {
            SuccessScreen(state)

        }
    }

}

@Composable
fun SuccessScreen(state: DetailsUiState.Success) {
    with(state.selected) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Header(
                    selectedRepository.repositoryOwner,
                    selectedRepository.watchers,
                    selectedRepository.forks,
                    selectedRepository.name
                )
            }
            itemsIndexed(tags, key = { index, item -> item.sha + index }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),

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
                            text = item.sha,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun Header(repositoryOwner: RepositoryOwner, watchers: Int, forks: Int, name: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            AsyncImage(
                model = repositoryOwner.avatar,
                contentDescription = "User image",
                modifier = Modifier.clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                repositoryOwner.name.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                name.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Fork count: $forks",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Watcher count: $watchers",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
