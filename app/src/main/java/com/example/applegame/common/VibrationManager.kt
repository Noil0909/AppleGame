package com.example.applegame.common

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object VibrationManager {

    // 토글 움직이는거 볼수있게함
    // state가 변해야 recomposition됨
    var isVibrationOn by mutableStateOf(true)

    fun initializeFromPrefs(context: Context) {
        isVibrationOn = SettingsRepository.isVibrationOn
    }


    fun vibrate(context: Context, duration: Long = 20L, amplitude: Int = 50) {
        if (!isVibrationOn) return

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // API 31 이상 (Android 12 이상)
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            // 그 이하 버전
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 이상
            val effect = VibrationEffect.createOneShot(duration, amplitude)
            vibrator.vibrate(effect)
        } else {
            // 그 이하 버전
            vibrator.vibrate(duration)
        }
    }
}