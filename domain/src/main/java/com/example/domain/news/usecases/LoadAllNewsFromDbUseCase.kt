package com.example.domain.news.usecases

import com.example.domain.news.repository.NewsRepository
import javax.inject.Inject

class LoadAllNewsFromDbUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke() = newsRepository.loadAllNewsFromDb()
}