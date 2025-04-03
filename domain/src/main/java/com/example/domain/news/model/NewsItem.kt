package com.example.domain.news.model

import kotlinx.serialization.Serializable

@Serializable
class NewsItem (
    val id: Int,
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
    val category: String
)