package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.utils.DragUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class AppleGameViewModel : ViewModel() {

    val rows = 18
    val cols = 9

    private val _grid = mutableStateListOf<MutableList<Int>>()
    val grid: List<List<Int>> get() = _grid

    var selectedCells by mutableStateOf(setOf<Pair<Int, Int>>())
        private set

    var score by mutableStateOf(0)
        private set

    var progress by mutableStateOf(1f)
        private set

    private var startRow = 0
    private var startCol = 0
    var cellSizePx = 1f  // 추후 계산용

    init {
        resetGame()
    }

    fun resetGame() {
        _grid.clear()
        repeat(rows) {
            _grid.add(MutableList(cols) { (1..9).random() })
        }
        score = 0
        selectedCells = emptySet()
        progress = 1f
    }

    fun startDrag(offsetX: Float, offsetY: Float) {
        val (r, c) = offsetToCell(offsetX, offsetY)
        startRow = r
        startCol = c
        selectedCells = emptySet()
    }

    fun updateDrag(offsetX: Float, offsetY: Float) {
        val (r2, c2) = offsetToCell(offsetX, offsetY)
        val r1 = min(startRow, r2)
        val r3 = max(startRow, r2)
        val c1 = min(startCol, c2)
        val c3 = max(startCol, c2)

        val selected = mutableSetOf<Pair<Int, Int>>()
        for (r in r1..r3) {
            for (c in c1..c3) {
                selected.add(r to c)
            }
        }
        selectedCells = selected
    }

    fun endDrag() {
        val sum = selectedCells.sumOf { (r, c) -> _grid[r][c] }
        if (sum == 10) {
            selectedCells.forEach { (r, c) ->
                _grid[r][c] = 0
            }
            score += selectedCells.size
        }
        selectedCells = emptySet()
    }

    private fun offsetToCell(x: Float, y: Float): Pair<Int, Int> {
        val col = (x / cellSizePx).toInt().coerceIn(0, cols - 1)
        val row = (y / cellSizePx).toInt().coerceIn(0, rows - 1)
        return row to col
    }

    fun startTimer(durationSec: Int = 120) {
        viewModelScope.launch {
            val total = durationSec * 1000
            val steps = 120  // 프레임 수
            val delayMs = total / steps
            for (i in 1..steps) {
                delay(delayMs.toLong())
                progress = 1f - i / steps.toFloat()
            }
        }
    }
}
