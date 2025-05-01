package com.example.applegame.common

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applegame.R

object SoundEffectManager {

    private var soundPool: SoundPool? = null
    private var popSoundId: Int? = null

    // 토글 움직이는거 볼수있게함
    // state가 변해야 recomposition됨
    var isSoundOn by mutableStateOf(true)

    fun initializeFromPrefs(context: Context) {
        isSoundOn = SettingsRepository.isSoundOn
    }


    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        popSoundId = soundPool?.load(context, R.raw.apple_sound, 1)

        isInitialized = true
    }

    fun playPopSound() {
        if (!isSoundOn) return // 꺼져있으면 재생 안함
        popSoundId?.let {
            soundPool?.play(it, 0.5f, 0.5f, 0, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
    }
}