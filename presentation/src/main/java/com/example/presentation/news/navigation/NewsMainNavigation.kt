package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.news.screen.NewsMainScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsMainScreenRoute

fun NavGraphBuilder.newsMainScreenDestination() {
    composable<NewsMainScreenRoute> {
        NewsMainScreen()
    }
}