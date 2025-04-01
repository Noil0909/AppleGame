package com.example.applegame.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.example.applegame.domain.model.Apple
import kotlin.math.abs
import kotlin.math.min

object DragUtils {
    fun createDragRect(start: Offset, end: Offset): Rect {
        return Rect(
            offset = Offset(min(start.x, end.x), min(start.y, end.y)),
            size = Size(abs(end.x - start.x), abs(end.y - start.y))
        )
    }

    fun calculateSelectedApples(
        apples: List<Apple>,
        dragRect: Rect,
        cellSizePx: Float,
        gridTopLeft: Offset
    ): List<Int> {
        return apples.filter { apple ->
            val row = apple.position / 10
            val col = apple.position % 10

            // gridTopLeft가 이미 로컬 좌표 기준이라면 아래처럼 계산
            val appleLeft = col * cellSizePx
            val appleTop = row * cellSizePx

            val appleRect = Rect(
                offset = Offset(appleLeft, appleTop),
                size = Size(cellSizePx, cellSizePx)
            )

            // 드래그 사각형과 사과 영역이 겹치면 선택 처리
            dragRect.overlaps(appleRect)
        }.map { it.id }
    }
}