package com.example.domain.universal.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveTheme(isDarkTheme: Boolean)

    fun getIsDarkTheme(): Flow<Boolean>
}