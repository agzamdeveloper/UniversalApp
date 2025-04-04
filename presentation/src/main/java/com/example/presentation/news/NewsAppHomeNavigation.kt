package com.example.presentation.news

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.presentation.news.screen.NewsApp
import com.example.presentation.news.screen.newsAppDestination
import com.example.presentation.news.screen.newsItemDestination
import kotlinx.serialization.Serializable

@Serializable
object NewsAppHome

fun NavGraphBuilder.newsAppHomeNavigationDestination(navController: NavHostController){
    navigation<NewsAppHome>(startDestination = NewsApp) {
        newsAppDestination(navController)
        newsItemDestination()
    }
}