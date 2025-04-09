package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.utils.DragUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        repeat(144) { index ->
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
    fun updateDragArea(
        start: Offset?,
        end: Offset?,
        cellSizePx: Float,
        gridTopLeft: Offset
    ) {
        // 드래그 좌표를 그리드 기준 로컬 좌표로 변환
        _dragStart.value = start?.minus(Offset.Zero)
        _dragEnd.value = end?.minus(Offset.Zero)
        _selectedIds.clear()

        val localStart = _dragStart.value
        val localEnd = _dragEnd.value

        if (localStart != null && localEnd != null) {
            val dragRect = DragUtils.createDragRect(localStart, localEnd)

            // 각 그리드의 위치를 기반으로 드래그된 사과를 선택
            _selectedIds.addAll(
                DragUtils.calculateSelectedApples(
                    apples = _apples,
                    dragRect = dragRect,
                    cellSizePx = cellSizePx,
                    gridTopLeft = Offset.Zero
                )
            )
        }
    }

    // 드래그 종료 시 선택 확인
    fun confirmSelection() {
        if (_selectedIds.sumOf { id ->
                _apples.first { it.id == id }.number
            } == 10) {
            removeMatchedApples()
        }
        _selectedIds.clear() // 선택 해제
        _dragStart.value = null
        _dragEnd.value = null
    }

    private fun removeMatchedApples() {
        _apples.replaceAll { apple ->
            if (apple.id in _selectedIds) apple.copy(number = 0) else apple
        }
        _score.value += _selectedIds.size * 100
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

    // === 게임 재시작 ===
    fun restartGame() {
        _remainingTime.value = 120
        _appleGameState.value = AppleGameState.Playing
        resetGame()
        startTimer()
    }
}