package com.example.domain.news.usecases

import com.example.domain.news.repository.NewsRepository
import javax.inject.Inject

class LoadNewsFromDbByIdUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(id: Int) = repository.loadNewsFromDbById(id)
}
