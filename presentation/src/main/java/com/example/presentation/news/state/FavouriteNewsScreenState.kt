package com.example.presentation.news.state

import com.example.domain.news.model.NewsItem

interface FavouriteNewsScreenState {
    data object FavouriteNewsInitial : FavouriteNewsScreenState
    data class FavouriteNewsSucceeded(val data: List<NewsItem>) : FavouriteNewsScreenState
}