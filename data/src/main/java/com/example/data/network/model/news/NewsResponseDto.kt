package com.example.data.network.model.news

class NewsResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsDto>
)
