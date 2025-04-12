package com.example.presentation.news.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.core.R
import com.example.domain.news.model.NewsItem
import com.example.presentation.enums.Category
import com.example.presentation.news.state.NewsScreenState
import com.example.presentation.news.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModule: NewsViewModel = hiltViewModel(),
    onNavigateToNewsDetailsScreen: (Int) -> Unit
) {
    val news = viewModule.newsState.collectAsState()
    val isInternetConnected = viewModule.isInternetConnected.collectAsState()
    var isRefreshing = viewModule.isRefreshing.collectAsState()

    when (val currentState = news.value) {
        is NewsScreenState.NewsStateSucceeded -> {
            NewsListContent(
                isInternetConnected = isInternetConnected.value,
                isRefreshing = isRefreshing.value,
                newsItemList = currentState.data,
                onClickCard = {
                    onNavigateToNewsDetailsScreen(it)
                },
                onClickDropdownItem = {
                    viewModule.loadNewsByCategoryFromDb(it)
                },
                onRefresh = {
                    viewModule.onPullToRefreshTrigger()
                }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListContent(
    isInternetConnected: Boolean,
    isRefreshing: Boolean,
    newsItemList: List<NewsItem>,
    onClickCard: (Int) -> Unit,
    onClickDropdownItem: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isInternetConnected) {
        if (isInternetConnected == false) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Internet is not connected",
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        Column{
            NewsListTop(
                onClickDropdownItem = { onClickDropdownItem(it) }
            )
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() }
            ) {
                LazyColumn {
                    items(newsItemList) { news ->
                        NewsListCard(
                            title = news.title,
                            imageUrl = news.urlToImage,
                            description = news.description,
                            onClickCard = {
                                onClickCard(news.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsListTop(
    onClickDropdownItem: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                text = "News app"
            )
        }
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (category in Category.entries) {
                    DropdownMenuItem(
                        text = { Text(category.value) },
                        onClick = {
                            expanded = !expanded
                            onClickDropdownItem(category.value)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NewsListCard(
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
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                text = title
            )
            Spacer(modifier = Modifier.height(4.dp))
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.error_image),
                error = painterResource(R.drawable.error_image),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description
            )
        }
    }
}