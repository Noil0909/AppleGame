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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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

@Composable
fun AppleGameScreen(viewModel: AppleGameViewModel = viewModel(),
                    onBackToMain: () -> Unit) {

    val grid = viewModel.grid
    val score = viewModel.score
    val progress = viewModel.progress
    val selected = viewModel.selectedCells

    var cellSize by remember { mutableStateOf(0f) }
    var gridOffset by remember { mutableStateOf(Offset.Zero) }


    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 정보 영역
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "남은 시간", fontSize = 16.sp)
            Text(text = "점수: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        // 타이머 ProgressBar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // AppleGrid (드래그 감지 포함)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val localOffset = offset - gridOffset
                            viewModel.startDrag(localOffset.x, localOffset.y)
                        },
                        onDrag = { change, _ ->
                            val localOffset = change.position - gridOffset
                            viewModel.updateDrag(localOffset.x, localOffset.y)
                        },
                        onDragEnd = {
                            viewModel.endDrag()
                        }
                    )
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.onGloballyPositioned {
                    val position = it.positionInRoot()
                    gridOffset = position  // 좌상단 좌표 기억
                }
            ) {
                grid.forEachIndexed { rowIndex, row ->
                    Row {
                        row.forEachIndexed { colIndex, value ->
                            AppleCell(
                                value = value,
                                isSelected = selected.contains(rowIndex to colIndex),
                                onSizeReady = {
                                    if (cellSize == 0f) {
                                        cellSize = it
                                        viewModel.cellSizePx = it
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
