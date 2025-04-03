package com.example.domain.weather.usecases

import com.example.domain.weather.repository.WeatherRepository
import javax.inject.Inject

class LoadCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke() = repository.loadCurrentWeather()
}