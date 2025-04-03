package com.example.presentation.weather.state

sealed interface WeatherScreenState {
    data object WeatherScreenStateFailure : WeatherScreenState

    data class WeatherStateSucceeded(
        val temperature: Int,
        val main: String,
        val description: String,
        val windSpeed: Double,
        val humidity: Int
    ) : WeatherScreenState

    data object WeatherScreenStateLoading : WeatherScreenState
}