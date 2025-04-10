package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
//import com.example.applegame.ui.utils.DragUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class AppleGameViewModel : ViewModel() {

    private val rows = 16
    private val cols = 9
    private val total = rows * cols

    private val _apples = MutableStateFlow<List<Apple>>(emptyList())
    val apples = _apples.asStateFlow()

    private val _score = mutableStateOf(0)
    val score: State<Int> get() = _score

    private val _selectedIds = mutableStateListOf<Int>()
    val selectedIds: List<Int> get() = _selectedIds

    private val _remainingTime = mutableStateOf(120)
    val remainingTime: Int get() = _remainingTime.value

    private val _appleGameState = mutableStateOf<AppleGameState>(AppleGameState.Playing)
    val appleGameState: AppleGameState get() = _appleGameState.value

    private val appleBounds = mutableMapOf<Int, Rect>()

    init {
        restartGame()
    }

    fun updateBounds(index: Int, rect: Rect) {
        appleBounds[index] = rect
    }

    fun handleDrag(start: Offset?, end: Offset?) {
        if (start == null || end == null) return

        val selectionRect = Rect(
            Offset(minOf(start.x, end.x), minOf(start.y, end.y)),
            Offset(maxOf(start.x, end.x), maxOf(start.y, end.y))
        )

        val updatedApples = _apples.value.map { apple ->
            val rect = appleBounds[apple.id]
            if (apple.visible && rect != null && rect.overlaps(selectionRect)) {
                apple.copy(isSelected = true)
            } else {
                apple.copy(isSelected = false)
            }
        }

        _apples.value = updatedApples
    }

    fun handleDragEnd() {
        val selected = _apples.value.filter { it.isSelected }
        val sum = selected.sumOf { it.number }

        if (sum == 10) {
            val selectedCount = selected.size
            _apples.value = _apples.value.map {
                if (it.isSelected) it.copy(visible = false, isSelected = false)
                else it.copy(isSelected = false)
            }
            _score.value += selectedCount * 100
        }
        else {
            _apples.value = _apples.value.map { it.copy(isSelected = false) }
        }
    }

    fun restartGame() {
        _score.value = 0
        _remainingTime.value = 120
        _appleGameState.value = AppleGameState.Playing
        _apples.value = generateGrid()
        appleBounds.clear()
        startTimer()
    }


    private fun generateGrid(): List<Apple> {
        return List(total) { id ->
            Apple(id = id, number = Random.nextInt(1, 10))
        }
    }

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
}