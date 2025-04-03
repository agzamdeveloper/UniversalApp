package com.example.presentation.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.settings.usecases.CheckInternetConnectionUseCase
import com.example.domain.weather.usecases.LoadCurrentWeatherUseCase
import com.example.presentation.weather.state.WeatherScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val loadCurrentWeatherUseCase: LoadCurrentWeatherUseCase,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherScreenState>(WeatherScreenState.WeatherScreenStateFailure)
    val weatherState: StateFlow<WeatherScreenState> = _weatherState.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            if (checkInternetConnectionUseCase()) {
                _weatherState.value = WeatherScreenState.WeatherScreenStateLoading
                val weather = loadCurrentWeatherUseCase()
                weather.let {
                    _weatherState.value = WeatherScreenState.WeatherStateSucceeded(
                        weather.temperature,
                        weather.main,
                        weather.description,
                        weather.windSpeed,
                        weather.humidity
                    )
                }
            } else {
                _weatherState.value = WeatherScreenState.WeatherScreenStateFailure
            }
        }
    }
}