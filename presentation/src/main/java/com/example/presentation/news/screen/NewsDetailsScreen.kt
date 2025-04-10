package com.example.presentation.news.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var fullContent = viewModule.fullContent.collectAsState()
    viewModule.loadNewsFromDbById(id)

    when (val currentState = newsItemState.value) {
        is NewsItemScreenState.NewsItemSucceeded -> {
            viewModule.loadFullContentByUrl(currentState.newsItem.url)

            NewsDetailsContentScreen(
                newsItem = currentState.newsItem,
                fullContent = fullContent.value,
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
    fullContent: String,
    onClickSaveInFavouritesButton: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsItem.urlToImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.error_image),
                error = painterResource(R.drawable.error_image),
                contentDescription = null
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = navigateBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_svgrepo_com),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = "Localized description"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        onClickSaveInFavouritesButton()
                    }
                ) {
                    Icon(
                        painter = if (newsItem.favourite) painterResource(R.drawable.saved)
                        else painterResource(R.drawable.unsaved),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                fontWeight = FontWeight.Bold,
                text = newsItem.source
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                fontWeight = FontWeight.Bold,
                text = newsItem.publishedAt
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = fullContent
        )
    }
}