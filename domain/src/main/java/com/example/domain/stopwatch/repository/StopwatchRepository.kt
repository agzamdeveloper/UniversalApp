package com.example.domain.stopwatch.repository

import kotlinx.coroutines.flow.Flow

interface StopwatchRepository {

    suspend fun saveTime(time: String)

    fun getTime(): Flow<String>

    fun playSound()
}