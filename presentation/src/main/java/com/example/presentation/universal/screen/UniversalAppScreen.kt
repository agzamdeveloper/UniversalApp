package com.example.presentation.universal.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.core.theme.UniversalAppTheme
import com.example.presentation.news.navigation.newsMainScreenDestination
import com.example.presentation.stopwatch.navigation.stopwatchScreenDestination
import com.example.presentation.universal.navigation.UniversalMainScreenRoute
import com.example.presentation.universal.navigation.universalMainScreenDestination
import com.example.presentation.universal.viewmodel.SettingsViewModel
import com.example.presentation.weather.navigation.weatherScreenDestination
import kotlinx.coroutines.launch

@Composable
fun UniversalAppScreen() {
    val navController = rememberNavController()
    val viewModel: SettingsViewModel = hiltViewModel()
    val isDarkTheme = viewModel.isDarkThemeState.collectAsState(false)

    UniversalAppTheme(darkTheme = isDarkTheme.value) {
        NavHost(
            navController = navController,
            startDestination = UniversalMainScreenRoute,
        ) {
            universalMainScreenDestination(navController)
            stopwatchScreenDestination(navController)
            weatherScreenDestination(navController)
            newsMainScreenDestination()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalMainScreen(
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
                    Spacer(modifier = Modifier.weight(1f))
                    UniversalAppSettings()
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
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Universal app"
                                )
                            }
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

@Composable
fun UniversalAppSettings(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var isDarkTheme = viewModel.isDarkThemeState.collectAsState(false)

    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Enable dark theme"
        )
        Switch(
            checked = isDarkTheme.value,
            onCheckedChange = {
                viewModel.enableDarkTheme(it)
            }
        )
    }
}