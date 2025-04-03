package com.example.domain.news.repository

import com.example.domain.news.model.NewsItem

interface NewsRepository {

    suspend fun loadAllNewsFromDb(): List<NewsItem>

    suspend fun loadNewsFromDbByCategory(category: String): List<NewsItem>

    suspend fun saveNewsInDbByCategory(category: String)
}