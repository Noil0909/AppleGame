package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class AppleGameViewModel : ViewModel() {

    private val _apples = mutableStateListOf<Apple>()
    val apples: List<Apple> get() = _apples

    private val _score = mutableStateOf(0)
    val score: State<Int> get() = _score

    private val _selectedIds = mutableStateListOf<Int>()
    val selectedIds: List<Int> get() = _selectedIds

    private val _remainingTime = mutableStateOf(120)
    val remainingTime: Int get() = _remainingTime.value

    private val _appleGameState = mutableStateOf<AppleGameState>(AppleGameState.Playing)
    val appleGameState: AppleGameState get() = _appleGameState.value

    // 드래그 영역 상태
    private val _dragStart = mutableStateOf<Offset?>(null)
    val dragStart: Offset? get() = _dragStart.value

    private val _dragEnd = mutableStateOf<Offset?>(null)
    val dragEnd: Offset? get() = _dragEnd.value

    init {
        resetGame()
        startTimer()
    }

    // === 게임 초기화 ===
    private fun resetGame() {
        _apples.clear()
        repeat(150) { index ->
            _apples.add(
                Apple(
                    id = index,
                    number = (1..9).random(),
                    position = index // position = 고정된 그리드 인덱스
                )
            )
        }
        _score.value = 0
        _selectedIds.clear()
    }

    // 드래그 영역 업데이트
    fun updateDragArea(start: Offset?, end: Offset?, cellSizePx: Float) {
        _dragStart.value = start
        _dragEnd.value = end
        _selectedIds.clear()

        if (start != null && end != null) {
            val (x1, y1) = start
            val (x2, y2) = end
            val minX = min(x1, x2)
            val maxX = max(x1, x2)
            val minY = min(y1, y2)
            val maxY = max(y1, y2)

            _apples.forEach { apple ->
                val row = apple.position / 10
                val col = apple.position % 10
                val centerX = (col + 0.5f) * cellSizePx
                val centerY = (row + 0.5f) * cellSizePx

                if (centerX in minX..maxX && centerY in minY..maxY) {
                    _selectedIds.add(apple.id)
                }
            }

            // 합계 10 확인
            if (_selectedIds.sumOf { id -> _apples.first { it.id == id }.number } == 10) {
                removeMatchedApples()
            }
        }
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
        _selectedIds.forEach { id ->
            val index = _apples.indexOfFirst { it.id == id }
            if (index != -1) {
                _apples[index] = _apples[index].copy(number = 0)
            }
        }
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