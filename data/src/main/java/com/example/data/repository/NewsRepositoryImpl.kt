package com.example.data.repository

import com.example.data.local.dao.NewsDao
import com.example.data.mapper.mapToDomainNewsItem
import com.example.data.mapper.mapToListNewsItemDbModel
import com.example.data.network.service.NewsApiService
import com.example.domain.news.model.NewsItem
import com.example.domain.news.repository.NewsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    override suspend fun loadAllNewsFromDb(): List<NewsItem> {
        val listOfNewsItem = mutableListOf<NewsItem>()
        val listOfNewsItemDbModel = newsDao.getAllNews().first()
        for (item in listOfNewsItemDbModel) {
            listOfNewsItem.add(item.mapToDomainNewsItem())
        }
        return listOfNewsItem
    }

    override suspend fun loadNewsFromDbByCategory(category: String): List<NewsItem> {
        val listOfNewsItem = mutableListOf<NewsItem>()
        val listOfNewsItemDbModel = newsDao.getNewsByCategory(category).first()
        for (item in listOfNewsItemDbModel) {
            listOfNewsItem.add(item.mapToDomainNewsItem())
        }
        return listOfNewsItem
    }

    override suspend fun loadNewsFromDbById(id: Int): NewsItem {
        return newsDao.getNewsFromBdById(id).mapToDomainNewsItem()
    }

    override suspend fun loadFavouriteNewsFromBd(): List<NewsItem> {
        val listOfNewsItem = mutableListOf<NewsItem>()
        val listOfNewsItemDbModel = newsDao.getFavouriteNewsFromBd().first()
        for (item in listOfNewsItemDbModel) {
            listOfNewsItem.add(item.mapToDomainNewsItem())
        }
        return listOfNewsItem
    }

    override suspend fun changeFavouriteStatus(id: Int, isFavourite: Boolean) {
        newsDao.changeFavouriteStatus(id, isFavourite)
    }

    override suspend fun saveNewsInDbByCategory(category: String) {
        val listOfNewsItemDbModel = apiService
            .loadAllNewsByCategory(category = category)
            .mapToListNewsItemDbModel(category)

        newsDao.saveAllNews(listOfNewsItemDbModel)
    }
}