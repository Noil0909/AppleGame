package com.example.applegame.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.domain.model.CircleItem
import com.example.applegame.ui.utils.DragUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class AppleGameViewModel : ViewModel() {
    private val rows = 16
    private val cols = 9
    private val total = rows * cols

    private val _circles = MutableStateFlow(generateGrid())
    val circles = _circles.asStateFlow()

    private val circleBounds = mutableMapOf<Int, Rect>()

    fun updateBounds(index: Int, rect: Rect) {
        circleBounds[index] = rect
    }

    fun handleDrag(start: Offset?, end: Offset?) {
        if (start == null || end == null) return

        val selectionRect = Rect(
            Offset(minOf(start.x, end.x), minOf(start.y, end.y)),
            Offset(maxOf(start.x, end.x), maxOf(start.y, end.y))
        )

        val updatedCircles = _circles.value.map { circle ->
            val rect = circleBounds[circle.index]
            if (circle.visible && rect != null && rect.overlaps(selectionRect)) {
                circle.copy(isSelected = true)
            } else {
                circle.copy(isSelected = false)
            }
        }

        _circles.value = updatedCircles
    }

    fun handleDragEnd() {
        val selected = _circles.value.filter { it.isSelected }
        val sum = selected.sumOf { it.number }

        if (sum == 10) {
            _circles.value = _circles.value.map {
                if (it.isSelected) it.copy(visible = false, isSelected = false)
                else it.copy(isSelected = false)
            }
        } else {
            _circles.value = _circles.value.map { it.copy(isSelected = false) }
        }
    }

    private fun generateGrid(): List<CircleItem> {
        return List(total) { index ->
            CircleItem(index = index, number = Random.nextInt(1, 10))
        }
    }
}
