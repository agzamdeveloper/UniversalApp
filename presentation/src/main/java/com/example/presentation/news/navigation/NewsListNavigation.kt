package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.presentation.news.screen.NewsListScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsListScreenRoute

fun NavGraphBuilder.newsListScreenDestination(navController: NavHostController) {
    composable<NewsListScreenRoute> {
        NewsListScreen(
            onNavigateToNewsDetailsScreen = {
                navController.navigate(route = NewsDetailsScreenRoute(it))
            }
        )
    }
}