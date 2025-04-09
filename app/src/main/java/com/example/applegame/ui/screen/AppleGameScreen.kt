package com.example.applegame.ui.screen

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.applegame.R
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.component.AppleCell
//import com.example.applegame.ui.component.AppleGrid
//import com.example.applegame.ui.component.DragSelectionBox
//import com.example.applegame.ui.component.GameInfoHeader
//import com.example.applegame.ui.component.GameOverDialog
//import com.example.applegame.ui.component.GameSettingsDialog
//import com.example.applegame.ui.component.TimeProgressBar
import com.example.applegame.ui.utils.DragUtils
import com.example.applegame.ui.viewmodel.AppleGameViewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun AppleGameScreen(viewModel: AppleGameViewModel = viewModel(),
                    onBackToMain: () -> Unit) {
    val circles by viewModel.circles.collectAsState()
    val rows = 16
    val cols = 9

    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragEnd by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragStart = it
                        dragEnd = it
                    },
                    onDrag = { change, _ ->
                        dragEnd = change.position
                        viewModel.handleDrag(dragStart, dragEnd)
                        change.consume()
                    },
                    onDragEnd = {
                        viewModel.handleDragEnd()
                        dragStart = null
                        dragEnd = null
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (col in 0 until cols) {
                        val index = row * cols + col
                        val circle = circles[index]

                        if (circle.visible) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (circle.isSelected) Color.Blue else Color.Cyan
                                    )
                                    .onGloballyPositioned { coords ->
                                        val pos = coords.positionInRoot()
                                        val size = coords.size.toSize()
                                        viewModel.updateBounds(index, androidx.compose.ui.geometry.Rect(pos, size))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${circle.number}", fontSize = 12.sp)
                            }
                        } else {
                            Spacer(modifier = Modifier.size(36.dp).padding(2.dp))
                        }
                    }
                }
            }
        }

        // Draw selection rectangle
        if (dragStart != null && dragEnd != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Red.copy(alpha = 0.3f),
                    topLeft = Offset(minOf(dragStart!!.x, dragEnd!!.x), minOf(dragStart!!.y, dragEnd!!.y)),
                    size = androidx.compose.ui.geometry.Size(
                        width = kotlin.math.abs(dragStart!!.x - dragEnd!!.x),
                        height = kotlin.math.abs(dragStart!!.y - dragEnd!!.y)
                    )
                )
            }
        }
    }
}
