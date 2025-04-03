package com.example.domain.stopwatch.usecases

import com.example.domain.stopwatch.repository.StopwatchRepository
import javax.inject.Inject

class GetTimeUseCase @Inject constructor(
    private val repository: StopwatchRepository
) {
    operator fun invoke() = repository.getTime()
}