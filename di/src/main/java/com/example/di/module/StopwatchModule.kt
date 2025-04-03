package com.example.di.module

import com.example.data.repository.StopwatchRepositoryImpl
import com.example.domain.stopwatch.repository.StopwatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StopwatchModule {

    @Binds
    fun bindStopwatchRepository(
        stopwatchRepositoryImpl: StopwatchRepositoryImpl
    ): StopwatchRepository
}