package com.example.applegame.ui.screen

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.applegame.R
import com.example.applegame.data.record.GameRecordDatabase
import com.example.applegame.data.record.GameRecordRepository
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.common.BgmManager
import com.example.applegame.ui.common.SoundEffectManager
import com.example.applegame.ui.common.SoundEffectManager.isSoundOn
import com.example.applegame.ui.common.VibrationManager
import com.example.applegame.ui.common.VibrationManager.isVibrationOn
import com.example.applegame.ui.component.GameInfoHeader
import com.example.applegame.ui.component.GameOverDialog
import com.example.applegame.ui.component.GameSettingsDialog
import com.example.applegame.ui.component.TimeProgressBar
import com.example.applegame.ui.viewmodel.AppleGameViewModel

@Composable
fun AppleGameScreen(
    onBackToMain: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { GameRecordDatabase.getInstance(context) }
    val repository = remember { GameRecordRepository(db.gameRecordDao()) }
    val viewModel = remember { AppleGameViewModel(repository) }

    val apples by viewModel.apples.collectAsState()
    val rows = 16
    val cols = 9

    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragEnd by remember { mutableStateOf<Offset?>(null) }

    var showSettings by remember { mutableStateOf(false) }
    var isBgmOn by rememberSaveable { mutableStateOf(BgmManager.isBgmOn) }

    LaunchedEffect(Unit) {
        SoundEffectManager.init(context)
    }

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

                        // 사과에 닿으면 진동
                        if (viewModel.isTouchingNewApple(dragStart, dragEnd)) {
                            VibrationManager.vibrate(context)
                        }

                        // 드래그할 때 매번 ViewModel에 알려줌
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
        // 상단 정보
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, bottom = 30.dp)
        ) {
            GameInfoHeader(viewModel = viewModel, onShowSettings = { showSettings = true })
            TimeProgressBar(viewModel)
            Spacer(modifier = Modifier.height(30.dp))


            AppleGameBoard(
                apples = apples,
                rows = rows,
                cols = cols,
                onUpdateBounds = viewModel::updateBounds,
                dragStart = dragStart,
                dragEnd = dragEnd
            )

            DragOverlay(
                dragStart = dragStart,
                dragEnd = dragEnd
            )
        }

        if (dragStart != null && dragEnd != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Red.copy(alpha = 0.3f),
                    topLeft = Offset(
                        minOf(dragStart!!.x, dragEnd!!.x),
                        minOf(dragStart!!.y, dragEnd!!.y)
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        width = kotlin.math.abs(dragStart!!.x - dragEnd!!.x),
                        height = kotlin.math.abs(dragStart!!.y - dragEnd!!.y)
                    )
                )
            }
        }

        // 설정 및 게임오버 다이얼로그
        GameSettingsDialog(
            showDialog = showSettings,
            onDismiss = { showSettings = false },
            isBgmOn = isBgmOn,
            onBgmToggle = { isOn ->
                isBgmOn = isOn                   // Compose 상태 업데이트
                BgmManager.isBgmOn = isOn       // 실제 BGM 로직 반영

                if (isOn) {
                    BgmManager.startBgm(context, R.raw.applegame_bgm)
                } else {
                    BgmManager.stopBgm()
                }
            },
            isSoundOn = isSoundOn,
            onSoundToggle = {
                isSoundOn = it
                SoundEffectManager.isSoundOn = it
            },
            isVibrationOn = isVibrationOn,
            onVibrationToggle = {
                isVibrationOn = it
                VibrationManager.isVibrationOn = it
            },
            showGoMainButton = true,
            showRestartButton = true,
            onRestart = { viewModel.restartGame() },
            onMainMenu = onBackToMain
        )

        if (viewModel.appleGameState is AppleGameState.GameOver) {
            GameOverDialog(
                score = (viewModel.appleGameState as AppleGameState.GameOver).score,
                onRestart = { viewModel.restartGame() },
                onMainMenu = onBackToMain,
                modifier = Modifier.width(300.dp)
            )
        }
    }
}
@Composable
fun AppleGameBoard(
    apples: List<Apple>,
    rows: Int,
    cols: Int,
    onUpdateBounds: (Int, Rect) -> Unit,
    dragStart: Offset?,
    dragEnd: Offset?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0 until cols) {
                    val index = row * cols + col
                    val apple = apples[index]

                    if (apple.visible) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(2.dp)
                                .onGloballyPositioned { coords ->
                                    val pos = coords.positionInWindow()
                                    val size = coords.size.toSize()
                                    onUpdateBounds(index, Rect(pos, size))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.apple2_icon),
                                contentDescription = null,
                                colorFilter = if(apple.isSelected) tint(Color.Red) else null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = if (apple.isSelected) 1.1f else 1f
                                        scaleY = if (apple.isSelected) 1.1f else 1f
                                    },
                            )
                            Text(
                                text = "${apple.number}",
                                fontSize = 15.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .offset(y=3.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(36.dp).padding(2.dp))
                    }
                }
            }
        }
    }
}

// 드래그영역
@Composable
fun DragOverlay(
    dragStart: Offset?,
    dragEnd: Offset?
) {
    if (dragStart != null && dragEnd != null) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Red.copy(alpha = 0.3f),
                topLeft = Offset(minOf(dragStart.x, dragEnd.x), minOf(dragStart.y, dragEnd.y)),
                size = androidx.compose.ui.geometry.Size(
                    width = kotlin.math.abs(dragStart.x - dragEnd.x),
                    height = kotlin.math.abs(dragStart.y - dragEnd.y)
                )
            )
        }
    }
}