package com.example.applegame.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.applegame.ui.utils.DragUtils
import com.example.applegame.ui.viewmodel.AppleGameViewModel

@Composable
fun BoxScope.DragSelectionBox(viewModel: AppleGameViewModel) {
    val density = LocalDensity.current
    val cellSizePx = with(density) {
        (LocalConfiguration.current.screenWidthDp.dp - 32.dp).toPx() / 10
    }

    Box(
        modifier = Modifier
            .matchParentSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        viewModel.updateDragArea(offset, offset, cellSizePx, Offset.Zero)
                    },
                    onDrag = { change, _ ->
                        viewModel.updateDragArea(
                            viewModel.dragStart?.plus(Offset.Zero), // gridTopLeft 대신 Offset.Zero
                            change.position,
                            cellSizePx,
                            Offset.Zero
                        )
                    },
                    onDragEnd = {
                        viewModel.confirmSelection()
                    }
                )
            }
    ) {
        val start = viewModel.dragStart
        val end = viewModel.dragEnd
        if (start != null && end != null) {
            val dragRect = DragUtils.createDragRect(start, end)
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color(0x664CAF50),
                    topLeft = dragRect.topLeft,
                    size = dragRect.size,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }
    }
}