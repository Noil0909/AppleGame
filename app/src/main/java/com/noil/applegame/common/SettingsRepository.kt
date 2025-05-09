package com.noil.applegame.common

import android.content.Context
import android.content.SharedPreferences

object SettingsRepository {
    private const val PREF_NAME = "game_settings"
    private const val KEY_BGM = "bgm_on"
    private const val KEY_SOUND = "sound_on"
    private const val KEY_VIBRATION = "vibration_on"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var isBgmOn: Boolean
        get() = prefs.getBoolean(KEY_BGM, true)
        set(value) = prefs.edit().putBoolean(KEY_BGM, value).apply()

    var isSoundOn: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var isVibrationOn: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION, value).apply()
}
