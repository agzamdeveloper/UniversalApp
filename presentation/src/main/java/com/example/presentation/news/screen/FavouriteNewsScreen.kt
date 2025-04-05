package com.example.presentation.news.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.news.state.FavouriteNewsScreenState
import com.example.presentation.news.viewmodel.NewsViewModel
import kotlinx.serialization.Serializable


@Serializable
object FavouriteNewsScreenRoute

fun NavGraphBuilder.favouriteNewsScreenDestination() {
    composable<FavouriteNewsScreenRoute> {
        FavouriteNewsScreen()
    }
}

@Composable
fun FavouriteNewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    viewModel.loadFavouriteNews()
    val favouriteNews = viewModel.favouriteNewsState.collectAsState()

    when (val currentState = favouriteNews.value) {
        is FavouriteNewsScreenState.FavouriteNewsSucceeded -> {
            LazyColumn {
                items(currentState.data) { news ->
                    NewsCard(
                        title = news.title,
                        imageUrl = news.urlToImage,
                        description = news.description,
                        onClickCard = {}
                    )
                }
            }
        }

        FavouriteNewsScreenState.FavouriteNewsInitial -> {
            Text(
                text = "Initial screen"
            )
        }
    }
}