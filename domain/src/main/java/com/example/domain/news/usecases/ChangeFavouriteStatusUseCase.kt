package com.example.domain.news.usecases

import com.example.domain.news.repository.NewsRepository
import javax.inject.Inject

class ChangeFavouriteStatusUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(id: Int, isFavourite: Boolean) =
        newsRepository.changeFavouriteStatus(id, isFavourite)
}