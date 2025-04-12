package com.example.presentation.news.state

import com.example.domain.news.model.NewsItem

sealed interface NewsScreenState {
    data object NewsStateLoading : NewsScreenState
    data class NewsStateSucceeded(val data: List<NewsItem>) : NewsScreenState
}