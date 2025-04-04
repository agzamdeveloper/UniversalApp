package com.example.presentation.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.news.model.NewsItem
import com.example.domain.news.usecases.ChangeFavouriteStatusUseCase
import com.example.domain.news.usecases.LoadAllNewsFromDbUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByCategoryUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByIdUseCase
import com.example.domain.news.usecases.SaveNewsInDbByCategoryUseCase
import com.example.domain.settings.usecases.CheckInternetConnectionUseCase
import com.example.presentation.news.state.NewsItemScreenState
import com.example.presentation.news.state.NewsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val loadNewsFromDbByIdUseCase: LoadNewsFromDbByIdUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val saveNewsInDbByCategoryUseCase: SaveNewsInDbByCategoryUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsScreenState>(NewsScreenState.NewsStateFailure)
    val newsState: StateFlow<NewsScreenState> = _newsState.asStateFlow()

    private val _newsItemState =
        MutableStateFlow<NewsItemScreenState>(NewsItemScreenState.NewsItemInitial)
    val newsItemState: StateFlow<NewsItemScreenState> = _newsItemState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var categoryChange = "general"
    private val categories =
        arrayOf("business", "entertainment", "health", "science", "sports", "technology") // todo

    init {
        //getNewsFromNetAndSaveInDb() //todo
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

    fun loadNewsFromDbById(id: Int) {
        viewModelScope.launch {
            val newsItem = loadNewsFromDbByIdUseCase(id)
            _newsItemState.value = NewsItemScreenState.NewsItemLoaded(newsItem)
        }
    }

    fun changeFavouriteStatusOfNewsItem(newsItem: NewsItem){
        viewModelScope.launch {
            _newsItemState.value = NewsItemScreenState.NewsItemLoaded(newsItem.copy(favourite = !newsItem.favourite))
            changeFavouriteStatusUseCase(newsItem.id, !newsItem.favourite)
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.value = true
        viewModelScope.launch {
            val news = loadNewsFromDbByCategoryUseCase(categoryChange)
            _newsState.value = NewsScreenState.NewsStateSucceeded(data = news)
            _isRefreshing.value = false
        }
    }
}