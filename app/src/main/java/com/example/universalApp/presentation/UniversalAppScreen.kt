package com.example.universalApp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.news.newsAppHomeNavigationDestination
import com.example.presentation.news.screen.NewsApp
import com.example.presentation.news.screen.newsAppDestination
import com.example.presentation.stopwatch.screen.StopwatchApp
import com.example.presentation.stopwatch.screen.stopwatchAppDestination
import com.example.presentation.weather.screen.WeatherApp
import com.example.presentation.weather.screen.weatherAppDestination
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Composable
fun UniversalAppScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = UniversalAppScreen,
    ) {
        universalAppDestination(navController)
        stopwatchAppDestination(navController)
        weatherAppDestination(navController)
        newsAppHomeNavigationDestination(navController)
    }
}


@Serializable
object UniversalAppScreen

fun NavGraphBuilder.universalAppDestination(navHostController: NavHostController) {
    composable<UniversalAppScreen> {
        UniversalScreen(
            onStopwatchClick = {
                navHostController.navigate(route = StopwatchApp)
            },
            onWeatherAppClick = {
                navHostController.navigate(route = WeatherApp)
            },
            onNewsAppClick = {
                navHostController.navigate(route = NewsApp)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalScreen(
    onStopwatchClick: () -> Unit,
    onWeatherAppClick: () -> Unit,
    onNewsAppClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    Button(
                        onClick = { onStopwatchClick() }
                    ) {
                        Text(
                            text = "Stopwatch app"
                        )
                    }
                    Button(
                        onClick = { onWeatherAppClick() }
                    ) {
                        Text(
                            text = "Weather app"
                        )
                    }
                    Button(
                        onClick = { onNewsAppClick() }
                    ) {
                        Text(
                            text = "News app"
                        )
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier,
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                            Text(
                                text = "Universal app"
                            )
                        }

                    }
                )
            }

        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Some text"
                )
            }
        }
    }
}