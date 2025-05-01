package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.data.record.GameRecordDatabase
import com.example.applegame.data.record.GameRecordRepository
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.common.SoundEffectManager
import kotlinx.coroutines.Job
//import com.example.applegame.ui.utils.DragUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class AppleGameViewModel(
    private val repository: GameRecordRepository
) : ViewModel() {

    private val rows = 16
    private val cols = 9
    private val total = rows * cols

    private val totalTime = 120 // 수정필요

    private val _apples = MutableStateFlow<List<Apple>>(emptyList())
    val apples = _apples.asStateFlow()

    private val _score = mutableStateOf(0)
    val score: State<Int> get() = _score

    private val _selectedIds = mutableStateListOf<Int>()
    val selectedIds: List<Int> get() = _selectedIds

    private val _remainingTime = mutableStateOf(totalTime) // 다이어로그 수정중
    val remainingTime: Int get() = _remainingTime.value

    private val _appleGameState = mutableStateOf<AppleGameState>(AppleGameState.Playing)
    val appleGameState: AppleGameState get() = _appleGameState.value

    private val appleBounds = mutableMapOf<Int, Rect>()

    private var timerJob: Job? = null

    override fun onCleared(){
        super.onCleared()
        timerJob?.cancel()
    }

    init {
        restartGame()
    }

    fun restartGame() {
        _score.value = 0
        _remainingTime.value = totalTime // 다이어로그 수정중
        _appleGameState.value = AppleGameState.Playing
        _apples.value = generateGrid()
        appleBounds.clear()
        startTimer()
    }


    fun updateBounds(index: Int, rect: Rect) {
        appleBounds[index] = rect
    }

    private fun generateGrid(): List<Apple> {
        return List(total) { id ->
            Apple(id = id, number = Random.nextInt(1, 10))
        }
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

            // 효과음
            SoundEffectManager.playPopSound()

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

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }

            // 0.5초 지연 후 게임 오버 트리거
            delay(1000)
            if (_appleGameState.value is AppleGameState.Playing) {
                triggerGameOver()
            }
        }
    }

    fun triggerGameOver() {
        if (_appleGameState.value !is AppleGameState.GameOver) {
            viewModelScope.launch {
                repository.insertRecord(_score.value) // ★ 점수 저장
            }
            _appleGameState.value = AppleGameState.GameOver(_score.value)
        }
    }

    fun isTouchingNewApple(start: Offset?, end: Offset?): Boolean {
        if (start == null || end == null) return false

        val selectionRect = Rect(
            Offset(minOf(start.x, end.x), minOf(start.y, end.y)),
            Offset(maxOf(start.x, end.x), maxOf(start.y, end.y))
        )

        // 드래그 박스에 새롭게 들어온 사과를 찾기
        return apples.value.any { apple ->
            apple.visible && !apple.isSelected && (appleBounds[apple.id]?.overlaps(selectionRect) == true)
        }
    }

}
