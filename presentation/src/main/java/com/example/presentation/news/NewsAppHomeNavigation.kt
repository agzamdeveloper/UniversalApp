package com.example.presentation.news

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.presentation.news.screen.FavouriteNewsScreenRoute
import com.example.presentation.news.screen.NewsApp
import com.example.presentation.news.screen.favouriteNewsScreenDestination
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

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home", NewsAppHome, Icons.Default.Home),
    TopLevelRoute("Favourites", FavouriteNewsScreenRoute, Icons.Default.Info)
)

@Serializable
object NewsAppRoute

fun NavGraphBuilder.newsAppNavigationDestination(){
    composable<NewsAppRoute> {
        NewsApp()
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NewsAppHome,
            modifier = Modifier.padding(innerPadding)
        ) {
            newsAppHomeNavigationDestination(navController)
            favouriteNewsScreenDestination()
        }
    }
}