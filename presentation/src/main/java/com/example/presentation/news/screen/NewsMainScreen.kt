package com.example.presentation.news.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.presentation.news.navigation.FavouriteNewsScreenRoute
import com.example.presentation.news.navigation.NewsDetailsScreenRoute
import com.example.presentation.news.navigation.NewsListScreenRoute
import com.example.presentation.news.navigation.NewsSettingsScreenRoute
import com.example.presentation.news.navigation.favouriteNewsScreenDestination
import com.example.presentation.news.navigation.newsDetailsScreenDestination
import com.example.presentation.news.navigation.newsListScreenDestination
import com.example.presentation.news.navigation.newsSettingsScreenDestination

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("NewsList", NewsListScreenRoute, Icons.Default.Home),
    TopLevelRoute("Favourites", FavouriteNewsScreenRoute, Icons.Default.Info),
    TopLevelRoute("Settings", NewsSettingsScreenRoute, Icons.Default.Settings)
)

@Composable
fun NewsMainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute?.contains(NewsDetailsScreenRoute::class.simpleName.toString()) != true) {
                NavigationBar {
                    topLevelRoutes.forEach { topLevelRoute ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    topLevelRoute.icon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = { Text(topLevelRoute.name) },
                            selected = selected,
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Blue,
                                selectedTextColor = Color.Blue,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NewsListScreenRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            newsListScreenDestination(navController)
            favouriteNewsScreenDestination(navController)
            newsSettingsScreenDestination()
            newsDetailsScreenDestination()
        }
    }
}