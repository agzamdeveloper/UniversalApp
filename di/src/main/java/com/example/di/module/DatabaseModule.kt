package com.example.di.module

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.NewsDao
import com.example.data.local.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideNewDatabase(@ApplicationContext context: Context): NewsDatabase{
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao{
        return database.newsDao()
    }

    private companion object{
        const val DATABASE_NAME = "news.db"
    }
}