package com.example.domain.news.usecases

import com.example.domain.news.repository.NewsRepository
import javax.inject.Inject

class LoadFavouriteNewsFromBdUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke() = repository.loadFavouriteNewsFromBd()
}