package com.example.domain.news.model

class News(
    val status: String,
    val totalResults: Int,
    val newsList: List<NewsItem>
)