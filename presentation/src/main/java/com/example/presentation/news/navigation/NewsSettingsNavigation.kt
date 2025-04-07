package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.news.screen.NewsSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object NewsSettingsScreenRoute

fun NavGraphBuilder.newsSettingsScreenDestination(){
    composable<NewsSettingsScreenRoute> {
        NewsSettingsScreen()
    }
}