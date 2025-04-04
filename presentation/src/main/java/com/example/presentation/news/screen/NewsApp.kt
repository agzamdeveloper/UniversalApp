package com.example.presentation.news.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.core.R
import com.example.presentation.news.state.NewsScreenState
import com.example.presentation.news.viewmodel.NewsViewModel
import kotlinx.serialization.Serializable

@Serializable
object NewsApp

fun NavGraphBuilder.newsAppDestination(navController: NavHostController) {
    composable<NewsApp> {
        NewsApp(
            onNavigateToNewsItemScreen = {
                navController.navigate(route = NewsItemScreenRoute(it))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(
    viewModule: NewsViewModel = hiltViewModel(),
    onNavigateToNewsItemScreen: (Int) -> Unit
) {
    val news = viewModule.newsState.collectAsState()
    var isRefreshing = viewModule.isRefreshing.collectAsState()

    when (val currentState = news.value) {
        is NewsScreenState.NewsStateSucceeded -> {
            Column {
                NewsTop(
                    onClickAllNews = { viewModule.loadAllNewsFromDb() },
                    onClickDropdownItem = { viewModule.loadNewsByCategory(it) }
                )
                PullToRefreshBox(
                    isRefreshing = isRefreshing.value,
                    onRefresh = { viewModule.onPullToRefreshTrigger() }
                ) {
                    LazyColumn {
                        items(currentState.data) { news ->
                            NewsCard(
                                title = news.title,
                                imageUrl = news.urlToImage,
                                description = news.description,
                                onClickCard = {
                                    onNavigateToNewsItemScreen(news.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        NewsScreenState.NewsStateFailure -> {
        }

        NewsScreenState.NewsStateLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun NewsTop(
    onClickAllNews: () -> Unit,
    onClickDropdownItem: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "News app"
        )
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All news") },
                onClick = { onClickAllNews() }
            )
            DropdownMenuItem(
                text = { Text("Business news") },
                onClick = { onClickDropdownItem("business") }
            )
            DropdownMenuItem(
                text = { Text("Entertainment news") },
                onClick = { onClickDropdownItem("entertainment") }
            )
            DropdownMenuItem(
                text = { Text("Health news") },
                onClick = { onClickDropdownItem("health") }
            )
            DropdownMenuItem(
                text = { Text("Science news") },
                onClick = { onClickDropdownItem("science") }
            )
            DropdownMenuItem(
                text = { Text("Sport news") },
                onClick = { onClickDropdownItem("sports") }
            )
            DropdownMenuItem(
                text = { Text("Technology news") },
                onClick = { onClickDropdownItem("technology") }
            )
        }
    }
}

@Composable
fun NewsCard(
    title: String,
    imageUrl: String,
    description: String,
    onClickCard: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp, 5.dp)
            .clickable(onClick = {
                onClickCard()
            })
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title
            )
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.error),
                contentDescription = null
            )
            Text(
                text = description
            )
        }
    }
}