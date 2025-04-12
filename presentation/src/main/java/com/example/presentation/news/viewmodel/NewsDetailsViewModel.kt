package com.example.presentation.news.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.news.model.NewsItem
import com.example.domain.news.usecases.ChangeFavouriteStatusUseCase
import com.example.domain.news.usecases.LoadFullContentByUrlUseCase
import com.example.domain.news.usecases.LoadNewsFromDbByIdUseCase
import com.example.presentation.news.state.NewsItemScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val loadNewsFromDbByIdUseCase: LoadNewsFromDbByIdUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val loadFullContentByUrlUseCase: LoadFullContentByUrlUseCase
): ViewModel() {

    init {
        Log.d("abc", "NewsDetailsViewModel initialized")
    }

    private val _newsItemState =
        MutableStateFlow<NewsItemScreenState>(NewsItemScreenState.NewsItemInitial)
    val newsItemState: StateFlow<NewsItemScreenState> = _newsItemState.asStateFlow()

    private val _fullContent = MutableStateFlow("Loading...")
    val fullContent: StateFlow<String> = _fullContent.asStateFlow()

    fun loadNewsFromDbById(id: Int) {
        viewModelScope.launch {
            val newsItem = loadNewsFromDbByIdUseCase(id)
            _newsItemState.value = NewsItemScreenState.NewsItemSucceeded(newsItem)
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
                e.printStackTrace()
                _fullContent.value = "Loading error"
            }
        }
    }
}