package com.example.domain.stopwatch.usecases

import com.example.domain.stopwatch.repository.StopwatchRepository
import javax.inject.Inject

class SaveTimeUseCase @Inject constructor(
    private val repository: StopwatchRepository
) {
    suspend operator fun invoke(time: String) = repository.saveTime(time)
}