package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppleGameViewModel : ViewModel() {

    private val _apples = mutableStateListOf<Apple>()
    val apples: List<Apple> get() = _apples

    private val _score = mutableStateOf(0)
    val score: State<Int> get() = _score

    private val _selectedIds = mutableStateListOf<Int>()
    val selectedIds: List<Int> get() = _selectedIds

    private val _remainingTime = mutableStateOf(60)
    val remainingTime: Int get() = _remainingTime.value

    private val _appleGameState = mutableStateOf<AppleGameState>(AppleGameState.Playing)
    val appleGameState: AppleGameState get() = _appleGameState.value

    init {
        resetGame()
        startTimer()
    }

    // === 게임 로직 ===
    private fun resetGame() {
        _apples.clear()
        repeat(150) { index ->
            _apples.add(Apple(id = index, number = (1..9).random()))
        }
        _score.value = 0
        _selectedIds.clear()
    }

    fun toggleApple(id: Int) {
        if (_selectedIds.contains(id)) {
            _selectedIds.remove(id)
        } else {
            _selectedIds.add(id)
            if (checkSum()) removeMatchedApples()
        }
    }

    private fun checkSum(): Boolean {
        return _selectedIds.sumOf { id ->
            _apples.first { it.id == id }.number
        } == 10
    }

    private fun removeMatchedApples() {
        _apples.removeAll { it.id in _selectedIds }
        _score.value += _selectedIds.size * 100
        _selectedIds.clear()
    }

    // === 타이머 로직 ===
    private fun startTimer() {
        viewModelScope.launch {
            while (_remainingTime.value > 0 && _appleGameState.value is AppleGameState.Playing) {
                delay(1000L)
                _remainingTime.value--
            }
            if (_remainingTime.value <= 0) {
                _appleGameState.value = AppleGameState.GameOver(_score.value)
            }
        }
    }

    // === 게임 제어 ===
    fun restartGame() {
        _remainingTime.value = 60
        _appleGameState.value = AppleGameState.Playing
        resetGame()
        startTimer()
    }
}