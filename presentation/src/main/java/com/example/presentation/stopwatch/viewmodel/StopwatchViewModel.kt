package com.example.presentation.stopwatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.stopwatch.usecases.GetTimeUseCase
import com.example.domain.stopwatch.usecases.PlaySoundUseCase
import com.example.domain.stopwatch.usecases.SaveTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val saveTimeUseCase: SaveTimeUseCase,
    private val getTimeUseCase: GetTimeUseCase,
    private val playSoundUseCase: PlaySoundUseCase
) : ViewModel() {
    private var time = 0
    private var formattedTime = ""
    private var timeJob: Job? = null

    val timeState: StateFlow<String> = getTimeUseCase().stateIn(
        viewModelScope,
        SharingStarted.Companion.Lazily,
        INITIAL_TIME
    )

    private val _isStarted = MutableStateFlow(false)
    val isStarted: StateFlow<Boolean> = _isStarted.asStateFlow()

    private val _firstStart = MutableStateFlow(false)
    val firstStart: StateFlow<Boolean> = _firstStart.asStateFlow()

    private val _laps = MutableStateFlow<List<String>>(emptyList())
    val laps: StateFlow<List<String>> = _laps.asStateFlow()

    init {
        viewModelScope.launch {
            saveTimeUseCase(INITIAL_TIME)
        }
    }

    companion object {
        private const val INITIAL_TIME = "00:00:00"
    }

    fun start() {
        playSoundUseCase()
        _firstStart.value = true
        _isStarted.value = true
        timeJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                time++
                formattedTime = formatTime(time)
                saveTimeUseCase(formattedTime)
            }
        }
    }

    fun pause() {
        playSoundUseCase()
        _isStarted.value = false
        timeJob?.cancel()
    }

    fun reset() {
        playSoundUseCase()
        viewModelScope.launch {
            _firstStart.value = false
            _laps.value = emptyList()
            timeJob?.cancel()
            time = 0
            saveTimeUseCase(INITIAL_TIME)
        }
    }

    fun lap() {
        playSoundUseCase()
        _laps.value += formattedTime
    }

    private fun formatTime(time: Int): String {
        val hours = time / 3600
        val remainderOfHours = time % 3600
        val minutes = remainderOfHours / 60
        val seconds = remainderOfHours % 60
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
}