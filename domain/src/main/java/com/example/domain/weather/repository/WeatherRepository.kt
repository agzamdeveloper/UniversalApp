package com.example.domain.weather.repository

import com.example.domain.weather.model.Weather

interface WeatherRepository {
    suspend fun loadCurrentWeather(): Weather
}