package com.example.domain.news.usecases

import com.example.domain.news.repository.NewsRepository
import javax.inject.Inject

class SaveNewsInDbByCategoryUseCase @Inject constructor(private val newsRepository: NewsRepository){
    suspend operator fun invoke(category: String) = newsRepository.saveNewsInDbByCategory(category)
}