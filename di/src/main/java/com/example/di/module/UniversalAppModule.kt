package com.example.di.module

import com.example.data.repository.SettingsRepositoryImpl
import com.example.domain.universal.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UniversalAppModule {

    @Binds
    fun bindsNewsSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}