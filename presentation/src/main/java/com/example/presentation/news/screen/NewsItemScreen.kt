package com.example.presentation.news.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.domain.news.model.NewsItem
import kotlinx.serialization.Serializable

@Serializable
class NewsItemScr(val newsItem: NewsItem)

fun NavGraphBuilder.newsItemDestination(){
    composable<NewsItemScr> { backStackEntry ->
        val newsItem = backStackEntry.toRoute<NewsItemScr>()

    }
}

@Composable
fun NewsItemScreen(
    newsItem: NewsItem
){
    Column {
        Text(
            text = newsItem.content
        )
        Text(
            text = newsItem.publishedAt
        )
        Text(
            text = newsItem.source
        )
    }
}