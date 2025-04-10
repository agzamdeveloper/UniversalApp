package com.example.domain.universal.usecases

import com.example.domain.universal.repository.SettingsRepository
import javax.inject.Inject

class GetIsDarkThemeUseCase @Inject constructor(
    private val newsSettingsRepository: SettingsRepository
) {
    operator fun invoke() = newsSettingsRepository.getIsDarkTheme()
}