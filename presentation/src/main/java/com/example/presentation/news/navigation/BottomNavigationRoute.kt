package com.example.presentation.news.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationRoute<T : Any>(val name: String, val route: T, val icon: ImageVector) {
    object NewsListBottomRoute :
        BottomNavigationRoute<NewsListScreenRoute>(
            "NewsList",
            NewsListScreenRoute,
            Icons.Default.Home
        )

    object FavouritesBottomRoute :
        BottomNavigationRoute<FavouriteNewsScreenRoute>(
            "Favourites",
            FavouriteNewsScreenRoute,
            Icons.Default.Info
        )

    companion object {
        val values = listOf(
            NewsListBottomRoute,
            FavouritesBottomRoute
        )
    }
}