package com.example.presentation.weather.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.R
import com.example.core.theme.backgroundLight
import com.example.core.theme.onSurfaceVariantDark
import com.example.core.theme.outlineDark
import com.example.core.theme.outlineVariantDark
import com.example.presentation.weather.state.WeatherScreenState
import com.example.presentation.weather.viewmodel.WeatherViewModel

@Composable
fun WeatherMainScreen(
    navigateBack: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState = viewModel.weatherState.collectAsState()

    when (val currentState = weatherState.value) {
        is WeatherScreenState.WeatherStateSucceeded -> {
            WeatherContentScreen(
                temperature = currentState.temperature,
                main = currentState.main,
                description = currentState.description,
                humidity = currentState.humidity,
                windSpeed = currentState.windSpeed,
                loadData = { viewModel.loadWeather() },
                navigateBack = navigateBack
            )
        }

        WeatherScreenState.WeatherScreenStateFailure -> {
            NotConnectionScreen(
                loadData = { viewModel.loadWeather() },
                navigateBack = navigateBack
            )
        }

        WeatherScreenState.WeatherScreenStateLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotConnectionScreen(
    loadData: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Weather app") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(bottom = 40.dp),
                painter = painterResource(R.drawable.wifi),
                contentDescription = "not connection"
            )
            Text(
                modifier = Modifier.padding(40.dp),
                text = "Check the internet connection and try again"
            )
            RefreshButton { loadData() }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WeatherContentScreen(
    temperature: Int,
    main: String,
    description: String,
    humidity: Int,
    windSpeed: Double,
    loadData: () -> Unit,
    navigateBack: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Weather app") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        FlowColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(
                    when (main) {
                        "Clear" -> Color.White
                        "Snow" -> backgroundLight
                        "Clouds", "Drizzle" -> onSurfaceVariantDark
                        "Rain", "Thunderstorm" -> outlineVariantDark
                        else -> outlineDark
                    }
                ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Center
        ) {
            WeatherMainInfo(
                temperature = temperature,
                main = main,
                description = description
            )
            WeatherAdditionalInfo(
                humidity = humidity,
                windSpeed = windSpeed
            )
            RefreshButton { loadData() }
        }
    }
}

@Composable
fun WeatherMainInfo(
    temperature: Int,
    main: String,
    description: String
) {
    Column(
        modifier = Modifier.width(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "Tashkent, Uzbekistan",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        ImageCard(
            main = main
        )
        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(200.dp),
            text = "${temperature}°C",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondaryContainer,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeatherAdditionalInfo(
    humidity: Int,
    windSpeed: Double
) {
    Row(
        modifier = Modifier.width(200.dp),
    ) {
        Card(
            modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.humidity),
                    contentDescription = "humidity"
                )
                Text(
                    text = "$humidity %",
                    fontSize = 18.sp
                )
            }
        }
        Spacer(
            modifier = Modifier.width(20.dp)
        )
        Card(
            modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.windy),
                    contentDescription = "windy"
                )
                Text(
                    text = "$windSpeed m/s",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun RefreshButton(
    loadData: () -> Unit
) {
    Column(
        modifier = Modifier.width(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            onClick = { loadData() }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun ImageCard(
    main: String
) {
    Image(
        modifier = Modifier.width(150.dp),
        painter = when (main) {
            "Clear" -> painterResource(R.drawable.clear_sky)
            "Clouds" -> painterResource(R.drawable.broken_clouds)
            "Rain" -> painterResource(R.drawable.shower_rain)
            "Drizzle" -> painterResource(R.drawable.rain)
            "Thunderstorm" -> painterResource(R.drawable.thunderstorm)
            "Snow" -> painterResource(R.drawable.snow)
            else -> painterResource(R.drawable.mist1)
        },
        contentDescription = "weather icon"
    )
}