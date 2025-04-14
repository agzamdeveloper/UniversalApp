package com.example.presentation.universal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.core.R
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
    onNewsAppClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isDarkTheme = viewModel.isDarkThemeState.collectAsState(false)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp)
            ) {
                Column(
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    NavigateTextButton(
                        buttonText = "Stopwatch",
                        painterResourceId = R.drawable.stopwatch_icon,
                        onClickTextButton = { onStopwatchClick() }
                    )
                    NavigateTextButton(
                        buttonText = "Weather",
                        painterResourceId = R.drawable.weather_icon,
                        onClickTextButton = { onWeatherAppClick() }
                    )
                    NavigateTextButton(
                        buttonText = "News",
                        painterResourceId = R.drawable.news_icon,
                        onClickTextButton = { onNewsAppClick() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    UniversalAppSettings(
                        isDarkTheme = isDarkTheme.value,
                        onDarkThemeChange = { viewModel.enableDarkTheme(it) }
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Universal app"
                        )
                    },
                    navigationIcon = {
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
                Image(
                    painter = painterResource(id = R.drawable.idea_generation),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Welcome!"
                )
            }
        }
    }
}

@Composable
fun NavigateTextButton(
    buttonText: String,
    painterResourceId: Int,
    onClickTextButton: () -> Unit
) {
    TextButton(
        onClick = { onClickTextButton() },
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(painterResourceId),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                fontSize = 20.sp,
                text = buttonText
            )
        }
    }
}

@Composable
fun UniversalAppSettings(
    isDarkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Enable dark theme"
        )
        Switch(
            checked = isDarkTheme,
            onCheckedChange = {
                onDarkThemeChange(it)
            }
        )
    }
}