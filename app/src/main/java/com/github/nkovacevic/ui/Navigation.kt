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

package com.github.nkovacevic.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.nkovacevic.ui.details.DetailsScreen
import com.github.nkovacevic.ui.repositories.RepositoryScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            RepositoryScreen(
                modifier = Modifier.padding(16.dp),
                onItemClicked = {
                    navController.navigate("details/${it.repositoryOwner.name}/${it.name}")
                }
            )
        }
        composable(
            "details/{userName}/{repoName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType },
                navArgument("repoName") { type = NavType.StringType })
        ) { backstackEntry ->
            DetailsScreen(
                userName = backstackEntry.arguments?.getString("userName")!!,
                repoName = backstackEntry.arguments?.getString("repoName")!!
            )
        }
    }
}
