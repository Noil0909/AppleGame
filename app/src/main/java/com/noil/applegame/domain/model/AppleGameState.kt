package com.noil.applegame.domain.model


sealed class AppleGameState {
    object Playing : AppleGameState()
    data class GameOver(val score: Int) : AppleGameState()
}
