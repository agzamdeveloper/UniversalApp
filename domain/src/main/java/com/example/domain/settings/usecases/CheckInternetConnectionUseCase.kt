package com.example.domain.settings.usecases

import com.example.domain.settings.repository.ConnectionRepository
import javax.inject.Inject

class CheckInternetConnectionUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    operator fun invoke() = repository.checkInternetConnection()
}