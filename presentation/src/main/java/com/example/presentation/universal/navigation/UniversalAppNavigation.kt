package com.example.presentation.universal.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.presentation.news.navigation.NewsMainScreenRoute
import com.example.presentation.stopwatch.navigation.StopwatchScreenRoute
import com.example.presentation.universal.screen.UniversalMainScreen
import com.example.presentation.weather.navigation.WeatherScreenRoute
import kotlinx.serialization.Serializable

@Serializable
object UniversalMainScreenRoute

fun NavGraphBuilder.universalMainScreenDestination(navHostController: NavHostController) {
    composable<UniversalMainScreenRoute> {
        UniversalMainScreen(
            onStopwatchClick = {
                navHostController.navigate(route = StopwatchScreenRoute)
            },
            onWeatherAppClick = {
                navHostController.navigate(route = WeatherScreenRoute)
            },
            onNewsAppClick = {
                navHostController.navigate(route = NewsMainScreenRoute)
            }
        )
    }
}