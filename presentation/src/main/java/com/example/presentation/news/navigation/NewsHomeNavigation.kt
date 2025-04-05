package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
object NewsHomeScreenRoute

fun NavGraphBuilder.newsHomeScreenDestination(navController: NavHostController) {
    navigation<NewsHomeScreenRoute>(startDestination = NewsListScreenRoute) {
        newsListScreenDestination(navController)
        newsDetailsScreenDestination()
    }
}