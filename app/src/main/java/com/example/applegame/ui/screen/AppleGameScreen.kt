package com.example.applegame.ui.screen

import android.graphics.ColorFilter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.applegame.R
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.component.GameOverDialog
import com.example.applegame.ui.viewmodel.AppleGameViewModel
import kotlin.math.abs
import kotlin.math.min

@Composable
fun AppleGameScreen(
    viewModel: AppleGameViewModel = viewModel(),
    onBackToMain: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val cellSize = (configuration.screenWidthDp.dp - 32.dp) / 10
    val cellSizePx = with(LocalDensity.current) { cellSize.toPx() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 점수
        GameInfoHeader(viewModel = viewModel)

        // 시간제한
        TimeProgressBar(viewModel = viewModel)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            viewModel.updateDragArea(offset, offset, cellSizePx)
                        },
                        onDrag = { change, _ ->
                            viewModel.updateDragArea(
                                viewModel.dragStart,
                                change.position,
                                cellSizePx
                            )
                        },
                        onDragEnd = {
                            viewModel.updateDragArea(null, null, cellSizePx)
                        }
                    )
                }
        ) {
            AppleGrid(viewModel, cellSize)
            DragSelectionBox(viewModel)
        }
    }
}

@Composable
private fun AppleGrid(
    viewModel: AppleGameViewModel,
    cellSize: Dp
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(viewModel.apples.sortedBy { it.position }, key = { it.id }) { apple ->
            if (apple.number != 0) {
                AppleItem(
                    apple = apple,
                    isSelected = viewModel.selectedIds.contains(apple.id),
                    cellSize = cellSize
                )
            }
        }
    }
}

@Composable
private fun AppleItem(
    apple: Apple,
    isSelected: Boolean,
    cellSize: Dp
) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.apple2_icon),
            contentDescription = "사과",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = if (isSelected) 1.2f else 1f
                    scaleY = if (isSelected) 1.2f else 1f
                },
            colorFilter = if (isSelected) tint(Color.Red) else null
        )
        Text(
            text = "${apple.number}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun DragSelectionBox(viewModel: AppleGameViewModel) {
    val dragStart = viewModel.dragStart
    val dragEnd = viewModel.dragEnd

    if (dragStart != null && dragEnd != null) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color(0x664CAF50),
                topLeft = Offset(
                    min(dragStart.x, dragEnd.x),
                    min(dragStart.y, dragEnd.y)
                ),
                size = Size(
                    abs(dragEnd.x - dragStart.x),
                    abs(dragEnd.y - dragStart.y)
                ),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}


@Composable
private fun GameInfoHeader(
    viewModel: AppleGameViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.apple1_icon),
                contentDescription = "점수",
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "${viewModel.score.value}",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFF6B6B),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(
            text = "${viewModel.remainingTime}초",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF4CAF50))
    }
}

@Composable
private fun TimeProgressBar(viewModel: AppleGameViewModel){
    val progress = viewModel.remainingTime / 120f // 1순위확인ㄱㄱ

    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(8.dp)),
        color = Color(0xFF4CAF50),
        trackColor = Color(0xFFC8E6C9),
        strokeCap = StrokeCap.Square
    )
}