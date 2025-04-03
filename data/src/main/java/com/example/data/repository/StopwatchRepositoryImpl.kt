package com.example.data.repository

import com.example.data.managers.DataStoreManager
import com.example.data.managers.SoundManager
import com.example.domain.stopwatch.repository.StopwatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StopwatchRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val soundManager: SoundManager
) : StopwatchRepository {

    override suspend fun saveTime(time: String) {
        dataStoreManager.saveTime(time)
    }

    override fun getTime(): Flow<String> {
        return dataStoreManager.getTime()
    }

    override fun playSound() {
        soundManager.playSound()
    }
}