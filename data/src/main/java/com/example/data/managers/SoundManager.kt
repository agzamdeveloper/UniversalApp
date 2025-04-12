package com.example.data.managers

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.core.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SoundManager @Inject constructor(@ApplicationContext context: Context) {

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_STREAMS)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val soundId = soundPool.load(context, R.raw.tick, 1)

    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    private companion object {
        const val MAX_STREAMS = 5
    }
}
