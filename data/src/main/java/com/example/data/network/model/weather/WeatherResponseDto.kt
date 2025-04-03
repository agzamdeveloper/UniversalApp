package com.example.data.network.model.weather

class WeatherResponseDto(
    val weather: List<WeatherDto>,
    val main: MainDto,
    val wind: WindDto
)
