package com.example.data.repository

import com.example.data.managers.DataStoreManager
import com.example.domain.universal.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override suspend fun saveTheme(isDarkTheme: Boolean) {
        dataStoreManager.saveTheme(isDarkTheme)
    }

    override fun getIsDarkTheme(): Flow<Boolean> {
        return dataStoreManager.getIsDarkTheme()
    }
}