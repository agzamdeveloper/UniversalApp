package com.example.data.network.service

import com.example.data.network.model.weather.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherApiService {
    @GET
    suspend fun loadTemperature(
        @Url fullUrl: String = FULL_URL,
        @Query(QUERY_PARAM_CITY) q: String = "Tashkent",
        @Query(QUERY_PARAM_KEY) appid: String = "aeff154cb987f9511d7d98ee12d54b67",
        @Query(QUERY_PARAM_UNITS) units: String = "metric"
    ): WeatherResponseDto

    companion object {
        private const val QUERY_PARAM_CITY = "q"
        private const val QUERY_PARAM_KEY = "appid"
        private const val QUERY_PARAM_UNITS = "units"

        private const val FULL_URL = "https://api.openweathermap.org/data/2.5/weather"
    }
}