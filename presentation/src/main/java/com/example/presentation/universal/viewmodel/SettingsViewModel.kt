package com.example.presentation.universal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.universal.usecases.GetIsDarkThemeUseCase
import com.example.domain.universal.usecases.SaveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getIsDarkThemeUseCase: GetIsDarkThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase
) : ViewModel() {

    val isDarkThemeState: Flow<Boolean> = getIsDarkThemeUseCase().stateIn(
        viewModelScope,
        SharingStarted.Companion.Lazily,
        false
    )

    fun enableDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            saveThemeUseCase(isDarkTheme)
        }
    }
}