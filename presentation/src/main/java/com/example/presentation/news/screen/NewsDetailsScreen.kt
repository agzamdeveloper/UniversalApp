package com.example.presentation.news.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.core.R
import com.example.domain.news.model.NewsItem
import com.example.presentation.news.state.NewsItemScreenState
import com.example.presentation.news.viewmodel.NewsViewModel

@Composable
fun NewsDetailsScreen(
    id: Int,
    navigateBack: () -> Unit,
    viewModule: NewsViewModel = hiltViewModel()
) {
    val newsItemState = viewModule.newsItemState.collectAsState()
    viewModule.loadNewsFromDbById(id)

    when (val currentState = newsItemState.value) {
        is NewsItemScreenState.NewsItemSucceeded -> {
            NewsDetailsContentScreen(
                newsItem = currentState.newsItem,
                onClickSaveInFavouritesButton = {
                    viewModule.changeFavouriteStatusOfNewsItem(currentState.newsItem)
                },
                navigateBack = {
                    navigateBack()
                }
            )
        }

        NewsItemScreenState.NewsItemInitial -> {
            Text(text = "This is initial screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsContentScreen(
    newsItem: NewsItem,
    onClickSaveInFavouritesButton: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "News details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.urlToImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.error),
                contentDescription = null
            )
            Text(
                text = newsItem.content
            )
            Text(
                text = newsItem.publishedAt
            )
            Text(
                text = newsItem.source
            )
            Button(
                onClick = {
                    onClickSaveInFavouritesButton()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (newsItem.favourite) Color.DarkGray else Color.White,    // Фон кнопки
                    contentColor = if (newsItem.favourite) Color.White else Color.Black    // Цвет текста
                )
            ) {
                Text(text = "Save in database")
            }
        }
    }

}