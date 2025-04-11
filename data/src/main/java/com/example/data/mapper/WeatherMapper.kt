package com.example.data.mapper

import com.example.data.network.model.weather.WeatherResponseDto
import com.example.domain.weather.model.Weather

internal fun WeatherResponseDto.mapResponseToDomainWeather(): Weather {
    val temperature = this.main.temp
    val humidity = this.main.humidity
    val windSpeed = this.wind.speed
    val main = this.weather.first().main
    val description = this.weather.first().description

    return Weather(
        temperature = temperature.toInt(),
        main = main,
        description = description,
        windSpeed = windSpeed,
        humidity = humidity
    )
}