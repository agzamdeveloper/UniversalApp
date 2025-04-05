package com.example.presentation.stopwatch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.presentation.stopwatch.screen.StopwatchMainScreen
import kotlinx.serialization.Serializable

@Serializable
object StopwatchScreenRoute

fun NavGraphBuilder.stopwatchScreenDestination(navHostController: NavHostController) {
    composable<StopwatchScreenRoute> {
        StopwatchMainScreen(navigateBack = { navHostController.popBackStack() })
    }
}