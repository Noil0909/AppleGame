package com.example.applegame.domain.model


sealed class AppleGameState {
    object Playing : AppleGameState()
    object Ready : AppleGameState()
    data class GameOver(val score: Int) : AppleGameState()
}
