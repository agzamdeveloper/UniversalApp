package com.example.presentation.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.presentation.news.screen.FavouriteNewsScreen
import kotlinx.serialization.Serializable

@Serializable
object FavouriteNewsScreenRoute

fun NavGraphBuilder.favouriteNewsScreenDestination(navController: NavHostController) {
    composable<FavouriteNewsScreenRoute> {
        FavouriteNewsScreen(
            onNavigateToNewsDetailsScreen = {
                navController.navigate(route = NewsDetailsScreenRoute(it))
            }
        )
    }
}