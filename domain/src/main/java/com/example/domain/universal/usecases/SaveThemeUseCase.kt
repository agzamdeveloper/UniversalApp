package com.example.domain.universal.usecases

import com.example.domain.universal.repository.SettingsRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val newsSettingsRepository: SettingsRepository
) {
    suspend operator fun invoke(isDarkTheme: Boolean) = newsSettingsRepository.saveTheme(isDarkTheme)
}