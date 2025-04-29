package com.example.applegame.ui.common

import android.content.Context
import android.media.MediaPlayer

object BgmManager {
    private var mediaPlayer: MediaPlayer? = null
    var isBgmOn: Boolean = true // ⚡ BGM on/off 상태 기억

    fun startBgm(context: Context, resId: Int) {
        if (!isBgmOn) return // bgm 꺼져있으면 시작 X

        stopBgm() // 🔁 항상 기존 BGM 중지 후 시작 (중복 방지 및 재시작 목적)
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            setVolume(0.3f, 0.3f)
            start()
        }
    }

    fun stopBgm() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    fun pauseBgm() {
        if (isBgmOn) {
            mediaPlayer?.pause()
        }
    }

    fun resumeBgm() {
        if (isBgmOn) {
            mediaPlayer?.start()
        }
    }

    fun toggleBgm(context: Context, resId: Int) {
        isBgmOn = !isBgmOn
        if (isBgmOn) {
            startBgm(context, resId)
        } else {
            stopBgm()
        }
    }
}