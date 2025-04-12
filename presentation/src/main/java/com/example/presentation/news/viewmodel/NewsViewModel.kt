package com.example.presentation.news.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.news.usecases.LoadAllNewsFromDbUseCase
import com.example.domain.news.usecases.LoadFavouriteNewsFromBdUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByCategoryUseCase
import com.example.domain.news.usecases.SaveNewsInDbByCategoryUseCase
import com.example.domain.settings.usecases.CheckInternetConnectionUseCase
import com.example.presentation.enums.Category
import com.example.presentation.news.state.FavouriteNewsScreenState
import com.example.presentation.news.state.NewsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    private val loadAllNewsFromDbUseCase: LoadAllNewsFromDbUseCase,
    private val loadNewsFromDbByCategoryUseCase: LoadNewsFromDbByCategoryUseCase,
    private val loadFavouriteNewsFromBdUseCase: LoadFavouriteNewsFromBdUseCase,
    private val saveNewsInDbByCategoryUseCase: SaveNewsInDbByCategoryUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsScreenState>(NewsScreenState.NewsStateLoading)
    val newsState: StateFlow<NewsScreenState> = _newsState.asStateFlow()

    private val _favoriteNewsState =
        MutableStateFlow<FavouriteNewsScreenState>(FavouriteNewsScreenState.FavouriteNewsInitial)
    val favouriteNewsState: StateFlow<FavouriteNewsScreenState> = _favoriteNewsState.asStateFlow()

    private val _isInternetConnected = MutableStateFlow(true)
    val isInternetConnected: StateFlow<Boolean> = _isInternetConnected.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        Log.d("abc", "NewsViewModel initialized")
        loadAllNewsFromDb()
    }

    private suspend fun getNewsFromNetAndSaveInDb() {
        _isInternetConnected.value = true
        delay(2000)
        if (checkInternetConnectionUseCase()) {
            for (category in Category.entries) {
                saveNewsInDbByCategoryUseCase(category.value)
            }
        } else {
            _isInternetConnected.value = false
        }
    }

    fun loadAllNewsFromDb() {
        Log.d("abc", "Load all news from db")
        viewModelScope.launch {
            getNewsFromNetAndSaveInDb()
            val news = loadAllNewsFromDbUseCase()
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = news)
        }
    }

    fun loadNewsByCategoryFromDb(category: String) {
        viewModelScope.launch {
            val news = if (category == Category.ALL_NEWS.value) {
                loadAllNewsFromDbUseCase()
            } else {
                loadNewsFromDbByCategoryUseCase(category)
            }
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = news)
        }
    }

    fun loadFavouriteNews() {
        viewModelScope.launch {
            val news = loadFavouriteNewsFromBdUseCase()
            _favoriteNewsState.value = FavouriteNewsScreenState.FavouriteNewsSucceeded(data = news)
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.value = true
        viewModelScope.launch {
            loadAllNewsFromDb()
            delay(2000)
            _isRefreshing.value = false
        }
    }
}