package com.example.presentation.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.news.model.NewsItem
import com.example.domain.news.usecases.LoadAllNewsFromDbUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByCategoryUseCase
import com.example.domain.news.usecases.SaveNewsInDbByCategoryUseCase
import com.example.domain.settings.usecases.CheckInternetConnectionUseCase
import com.example.presentation.news.state.NewsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModule @Inject constructor(
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    private val loadAllNewsFromDbUseCase: LoadAllNewsFromDbUseCase,
    private val loadNewsFromDbByCategoryUseCase: LoadNewsFromDbByCategoryUseCase,
    private val saveNewsInDbByCategoryUseCase: SaveNewsInDbByCategoryUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsScreenState>(NewsScreenState.NewsStateFailure)
    val newsState: StateFlow<NewsScreenState> = _newsState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var categoryChange = "general"
    private val categories =
        arrayOf("business", "entertainment", "health", "science", "sports", "technology") // todo

    init {
        getNewsFromNetAndSaveInDb()
        loadAllNewsFromDb()
    }

    fun getNewsFromNetAndSaveInDb() {
        _newsState.value = NewsScreenState.NewsStateLoading
        viewModelScope.launch {
            if (checkInternetConnectionUseCase()) {
                for (category in categories) {
                    saveNewsInDbByCategoryUseCase(category)
                }
            }
        }
    }

    fun loadAllNewsFromDb() {
        viewModelScope.launch {
            val newsItemList = loadAllNewsFromDbUseCase()
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = newsItemList)
        }
    }

    fun loadNewsByCategory(category: String) {
        categoryChange = category
        viewModelScope.launch {
            val news = loadNewsFromDbByCategoryUseCase(category)
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = news)
        }
    }

    fun loadNewsById(item: NewsItem) {

    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.value = true
        viewModelScope.launch {
            val news = loadNewsFromDbByCategoryUseCase("health")
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = news)
            _isRefreshing.value = false
        }
    }
}