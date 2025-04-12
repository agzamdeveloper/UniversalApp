package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.model.NewsItemDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsItemDbModel>>

    @Query("SELECT * FROM news WHERE category == :category")
    fun getNewsByCategory(category: String): Flow<List<NewsItemDbModel>>

    @Query("SELECT * FROM news WHERE id == :id")
    suspend fun getNewsFromBdById(id: Int): NewsItemDbModel

    @Query("SELECT * FROM news WHERE favourite == 1")
    fun getFavouriteNewsFromBd(): Flow<List<NewsItemDbModel>>

    @Query("UPDATE news SET favourite = :isFavourite WHERE id == :id")
    suspend fun changeFavouriteStatus(id: Int, isFavourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveAllNews(newsList: List<NewsItemDbModel>)
}