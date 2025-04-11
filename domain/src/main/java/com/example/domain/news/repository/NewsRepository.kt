package com.example.domain.news.repository

import com.example.domain.news.model.NewsItem

interface NewsRepository {

    suspend fun loadAllNewsFromDb(): List<NewsItem>

    suspend fun loadNewsFromDbByCategory(category: String): List<NewsItem>

    suspend fun loadNewsFromDbById(id: Int): NewsItem

    suspend fun loadFavouriteNewsFromBd(): List<NewsItem>

    suspend fun changeFavouriteStatus(id: Int, isFavourite: Boolean)

    suspend fun saveNewsInDbByCategory(category: String)

    suspend fun loadFullContentByUrl(url: String): String
}