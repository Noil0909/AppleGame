package com.example.applegame.data.local

import android.content.Context

class LocalDataSource(context: Context) {
    private val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    // 최고 점수 저장
    fun saveHighScore(score: Int) {
        prefs.edit().putInt("high_score", score).apply()
    }

    // 최고 점수 불러오기
    fun getHighScore(): Int = prefs.getInt("high_score", 0)
}