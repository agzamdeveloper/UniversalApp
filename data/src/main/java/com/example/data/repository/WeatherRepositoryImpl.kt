package com.example.data.repository

import com.example.data.mapper.mapResponseToDomainWeather
import com.example.data.network.service.WeatherApiService
import com.example.domain.weather.model.Weather
import com.example.domain.weather.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun loadCurrentWeather(): Weather {
        return apiService.loadTemperature().mapResponseToDomainWeather()
    }
}
