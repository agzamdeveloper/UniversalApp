package com.example.presentation.stopwatch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.core.R
import com.example.presentation.stopwatch.viewmodel.StopwatchViewModel
import kotlinx.serialization.Serializable

@Serializable
object StopwatchApp

fun NavGraphBuilder.stopwatchAppDestination(navHostController: NavHostController) {
    composable<StopwatchApp> {
        StopwatchApp(navigateBack = { navHostController.popBackStack() })
    }
}

@Composable
fun StopwatchApp(
    navigateBack: () -> Unit,
    stopwatchViewModel: StopwatchViewModel = hiltViewModel()
) {
    val time = stopwatchViewModel.timeState.collectAsState()
    val isStarted = stopwatchViewModel.isStarted.collectAsState()
    val firstStart = stopwatchViewModel.firstStart.collectAsState()
    val lapsList = stopwatchViewModel.laps.collectAsState()

    StopwatchScreen(
        time = time.value,
        lapsList = lapsList.value,
        isStarted = isStarted.value,
        firstStart = firstStart.value,
        start = { stopwatchViewModel.start() },
        pause = { stopwatchViewModel.pause() },
        lap = { stopwatchViewModel.lap() },
        reset = { stopwatchViewModel.reset() },
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchScreen(
    time: String,
    lapsList: List<String>,
    isStarted: Boolean,
    firstStart: Boolean,
    start: () -> Unit,
    pause: () -> Unit,
    lap: () -> Unit,
    reset: () -> Unit,
    navigateBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Stopwatch app") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                text = time
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Box(
                modifier = Modifier.height(200.dp)
            ) {
                LaunchedEffect(lapsList.size) {
                    listState.animateScrollToItem((lapsList.size + 1) - 1)
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(lapsList) { lap ->
                        Text(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            text = lap
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            Row {
                StartPauseButton(
                    isStarted = isStarted,
                    onClickIfStarted = { pause() },
                    onClickIfNotStarted = { start() }
                )
                if (firstStart) {
                    Spacer(
                        modifier = Modifier.width(90.dp)
                    )
                    LapResetButton(
                        isStarted = isStarted,
                        onClickIfStarted = { lap() },
                        onClickIfNotStarted = { reset() }
                    )
                }
            }
        }
    }
}

@Composable
fun StartPauseButton(
    isStarted: Boolean,
    onClickIfStarted: () -> Unit,
    onClickIfNotStarted: () -> Unit
) {
    RoundedIconButton(
        isStarted = isStarted,
        onClickIfStarted = onClickIfStarted,
        onClickIfNotStarted = onClickIfNotStarted,
        iconIfStarted = painterResource(R.drawable.pause),
        iconIfNotStarted = painterResource(R.drawable.play)
    )
}

@Composable
fun LapResetButton(
    isStarted: Boolean,
    onClickIfStarted: () -> Unit,
    onClickIfNotStarted: () -> Unit
) {
    RoundedIconButton(
        isStarted = isStarted,
        onClickIfStarted = onClickIfStarted,
        onClickIfNotStarted = onClickIfNotStarted,
        iconIfStarted = painterResource(R.drawable.flag),
        iconIfNotStarted = painterResource(R.drawable.stop)
    )
}

@Composable
fun RoundedIconButton(
    isStarted: Boolean,
    onClickIfStarted: () -> Unit,
    onClickIfNotStarted: () -> Unit,
    iconIfStarted: Painter,
    iconIfNotStarted: Painter,
) {
    IconButton(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        onClick = {
            if (isStarted) {
                onClickIfStarted()
            } else {
                onClickIfNotStarted()
            }
        }
    ) {
        Icon(
            painter = if (isStarted) iconIfStarted else iconIfNotStarted,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
    }
}