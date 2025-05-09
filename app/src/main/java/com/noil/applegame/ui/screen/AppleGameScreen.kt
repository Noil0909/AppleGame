package com.noil.applegame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.noil.applegame.R
import com.noil.applegame.data.record.GameRecordDatabase
import com.noil.applegame.data.record.GameRecordRepository
import com.noil.applegame.domain.model.Apple
import com.noil.applegame.domain.model.AppleGameState
import com.noil.applegame.common.BgmManager
import com.noil.applegame.common.SettingsRepository
import com.noil.applegame.common.SoundEffectManager
import com.noil.applegame.common.VibrationManager
import com.noil.applegame.ui.component.GameInfoHeader
import com.noil.applegame.ui.component.GameOverDialog
import com.noil.applegame.ui.component.GameSettingsDialog
import com.noil.applegame.ui.component.TimeProgressBar
import com.noil.applegame.ui.viewmodel.AppleGameViewModel

val jalNanFont = FontFamily(
    Font(R.font.jalnan2)
)

@Composable
fun AppleGameScreen(
    onBackToMain: () -> Unit
) {
    val context = LocalContext.current

    var isBgmOn by rememberSaveable { mutableStateOf(SettingsRepository.isBgmOn) }
    var isSoundOn by rememberSaveable { mutableStateOf(SettingsRepository.isSoundOn) }
    var isVibrationOn by rememberSaveable { mutableStateOf(SettingsRepository.isVibrationOn) }

    val db = remember { GameRecordDatabase.getInstance(context) }
    val repository = remember { GameRecordRepository(db.gameRecordDao()) }
    val viewModel = remember { AppleGameViewModel(repository) }

    val apples by viewModel.apples.collectAsState()
    val rows = 16
    val cols = 9

    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragEnd by remember { mutableStateOf<Offset?>(null) }

    var showSettings by remember { mutableStateOf(false) }

    var isRotated by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        SettingsRepository.init(context)

        SoundEffectManager.initializeFromPrefs(context)
        VibrationManager.initializeFromPrefs(context)

        if (BgmManager.isBgmOn) {
            BgmManager.startBgm(context, R.raw.applegame_bgm)
        }

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
            GameInfoHeader(
                viewModel = viewModel,
                onShowSettings = { showSettings = true },
                onRotateToggle = { isRotated = !isRotated }
            )
            TimeProgressBar(viewModel)
            Spacer(modifier = Modifier.height(30.dp))


            AppleGameBoard(
                apples = apples,
                rows = rows,
                cols = cols,
                onUpdateBounds = viewModel::updateBounds,
                dragStart = dragStart,
                dragEnd = dragEnd,
                rotateContent = isRotated
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

        // 게임설정 및 게임오버 다이얼로그
        GameSettingsDialog(
            showDialog = showSettings,
            onDismiss = { showSettings = false },
            isBgmOn = isBgmOn,
            onBgmToggle = {
                isBgmOn = it
                BgmManager.isBgmOn = it
                SettingsRepository.isBgmOn = it
                if (it) BgmManager.startBgm(context, R.raw.applegame_bgm) else BgmManager.stopBgm()
            },
            isSoundOn = isSoundOn,
            onSoundToggle = {
                isSoundOn = it
                SoundEffectManager.isSoundOn = it
                SettingsRepository.isSoundOn = it
            },
            isVibrationOn = isVibrationOn,
            onVibrationToggle = {
                isVibrationOn = it
                VibrationManager.isVibrationOn = it
                SettingsRepository.isVibrationOn = it
            },
            showGoMainButton = true,
            showRestartButton = true,
            onRestart = { viewModel.restartGame() },
            onMainMenu = onBackToMain
        )

        if (viewModel.appleGameState is AppleGameState.GameOver) {
            val score = (viewModel.appleGameState as AppleGameState.GameOver).score

            // 한번만 저장되도록 key는 score
            LaunchedEffect(score) {
                viewModel.saveRecord(score)
            }

            GameOverDialog(
                score = score,
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
    rotateContent: Boolean,
    dragStart: Offset?,
    dragEnd: Offset?
) {
    // painterResource는 내부적으로 remember로 활용. 여러번 호출해도 메모리 낭비X
    val applePainter = painterResource(id = R.drawable.apple2_icon)

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
                        AppleCell(
                            apple = apple,
                            index = index,
                            painter = applePainter,
                            onUpdateBounds = onUpdateBounds,
                            rotateContent = rotateContent
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun AppleCell(
    apple: Apple,
    index: Int,
    painter: Painter,
    onUpdateBounds: (Int, Rect) -> Unit,
    rotateContent: Boolean = false

) {
    val rotationAngle = if (rotateContent) 90f else 0f
    val offsetModifier = if (rotateContent) {
        Modifier.offset(x = (-4).dp)
    } else {
        Modifier.offset(y = 4.dp)
    }

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
            painter = painter,
            contentDescription = null,
            colorFilter = if (apple.isSelected) ColorFilter.tint(Color.Red) else null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = if (apple.isSelected) 1.1f else 1f
                    scaleY = if (apple.isSelected) 1.1f else 1f
                    rotationZ = rotationAngle
                }
        )
        Text(
            text = "${apple.number}",
            fontSize = 13.sp,
            color = Color.White,
            fontWeight = FontWeight.Thin,
            style = TextStyle(fontFamily = jalNanFont),
            modifier = offsetModifier.graphicsLayer {
                rotationZ = if (rotateContent) 90f else 0f
            }
        )
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