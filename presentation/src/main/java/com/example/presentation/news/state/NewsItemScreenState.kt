package com.example.presentation.news.state

import com.example.domain.news.model.NewsItem

interface NewsItemScreenState {
    data object NewsItemInitial: NewsItemScreenState
    data class NewsItemLoaded(val newsItem: NewsItem): NewsItemScreenState
}