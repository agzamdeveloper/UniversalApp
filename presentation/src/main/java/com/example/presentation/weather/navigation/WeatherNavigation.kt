package com.example.presentation.weather.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.presentation.weather.screen.WeatherMainScreen
import kotlinx.serialization.Serializable

@Serializable
object WeatherScreenRoute

fun NavGraphBuilder.weatherScreenDestination(navHostController: NavHostController) {
    composable<WeatherScreenRoute> {
        WeatherMainScreen(navigateBack = { navHostController.popBackStack() })
    }
}