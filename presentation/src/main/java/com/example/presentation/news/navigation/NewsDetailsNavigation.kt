package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.presentation.news.screen.NewsDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class NewsDetailsScreenRoute(val id: Int)

fun NavGraphBuilder.newsDetailsScreenDestination() {
    composable<NewsDetailsScreenRoute> {
        val route = it.toRoute<NewsDetailsScreenRoute>()
        NewsDetailsScreen(route.id)
    }
}