package com.example.applegame.common

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object BgmManager {
    private var mediaPlayer: MediaPlayer? = null
    var isBgmOn by mutableStateOf(true)

    fun initializeFromPrefs(context: Context) {
        isBgmOn = SettingsRepository.isBgmOn
    }

    fun startBgm(context: Context, resId: Int) {
        if (!isBgmOn) return
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                isLooping = true
                setVolume(0.3f, 0.3f)
                start()
            }
        } else {
            mediaPlayer?.start()
        }
    }

    fun pauseBgm() {
        mediaPlayer?.pause()
    }

    fun stopBgm() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}