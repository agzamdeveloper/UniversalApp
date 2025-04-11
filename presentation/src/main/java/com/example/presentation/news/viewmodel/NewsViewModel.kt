package com.example.presentation.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.news.model.NewsItem
import com.example.domain.news.usecases.ChangeFavouriteStatusUseCase
import com.example.domain.news.usecases.LoadAllNewsFromDbUseCase
import com.example.domain.news.usecases.LoadFavouriteNewsFromBdUseCase
import com.example.domain.news.usecases.LoadFullContentByUrlUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByCategoryUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByIdUseCase
import com.example.domain.news.usecases.SaveNewsInDbByCategoryUseCase
import com.example.domain.settings.usecases.CheckInternetConnectionUseCase
import com.example.presentation.enums.Category
import com.example.presentation.news.state.FavouriteNewsScreenState
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
    private val loadFavouriteNewsFromBdUseCase: LoadFavouriteNewsFromBdUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val saveNewsInDbByCategoryUseCase: SaveNewsInDbByCategoryUseCase,
    private val loadFullContentByUrlUseCase: LoadFullContentByUrlUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsScreenState>(NewsScreenState.NewsStateFailure)
    val newsState: StateFlow<NewsScreenState> = _newsState.asStateFlow()

    private val _newsItemState =
        MutableStateFlow<NewsItemScreenState>(NewsItemScreenState.NewsItemInitial)
    val newsItemState: StateFlow<NewsItemScreenState> = _newsItemState.asStateFlow()

    private val _favoriteNewsState =
        MutableStateFlow<FavouriteNewsScreenState>(FavouriteNewsScreenState.FavouriteNewsInitial)
    val favouriteNewsState: StateFlow<FavouriteNewsScreenState> = _favoriteNewsState.asStateFlow()

    private val _fullContent = MutableStateFlow("Loading...")
    val fullContent: StateFlow<String> = _fullContent.asStateFlow()

    private val _isInternetConnected = MutableStateFlow(true)
    val isInternetConnected: StateFlow<Boolean> = _isInternetConnected.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadAllNewsFromDb()
    }

    private suspend fun getNewsFromNetAndSaveInDb() {
        if (checkInternetConnectionUseCase()) {
            _isInternetConnected.value = true
            for (category in Category.entries) {
                saveNewsInDbByCategoryUseCase(category.value)
            }
        } else {
            _isInternetConnected.value = false
        }
    }

    fun loadAllNewsFromDb() {
        _newsState.value = NewsScreenState.NewsStateLoading
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

    fun loadNewsFromDbById(id: Int) {
        viewModelScope.launch {
            val newsItem = loadNewsFromDbByIdUseCase(id)
            _newsItemState.value = NewsItemScreenState.NewsItemSucceeded(newsItem)
        }
    }

    fun loadFavouriteNews() {
        viewModelScope.launch {
            val news = loadFavouriteNewsFromBdUseCase()
            _favoriteNewsState.value = FavouriteNewsScreenState.FavouriteNewsSucceeded(data = news)
        }
    }

    fun changeFavouriteStatusOfNewsItem(newsItem: NewsItem) {
        viewModelScope.launch {
            _newsItemState.value =
                NewsItemScreenState.NewsItemSucceeded(newsItem.copy(favourite = !newsItem.favourite))

            changeFavouriteStatusUseCase(newsItem.id, !newsItem.favourite)
        }
    }

    fun loadFullContentByUrl(url: String) {
        viewModelScope.launch {
            try {
                val content = loadFullContentByUrlUseCase(url)
                _fullContent.value = content
            } catch (e: Exception) {
                _fullContent.value = "Loading error"
            }
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.value = true
        viewModelScope.launch {
            loadAllNewsFromDb()
            _isRefreshing.value = false
        }
    }
}