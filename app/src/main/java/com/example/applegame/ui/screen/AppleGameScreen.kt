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
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.applegame.R
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.component.AppleGrid
import com.example.applegame.ui.component.DragSelectionBox
import com.example.applegame.ui.component.GameInfoHeader
import com.example.applegame.ui.component.GameOverDialog
import com.example.applegame.ui.component.GameSettingsDialog
import com.example.applegame.ui.component.TimeProgressBar
import com.example.applegame.ui.utils.DragUtils
import com.example.applegame.ui.viewmodel.AppleGameViewModel

@Composable
fun AppleGameScreen(
    viewModel: AppleGameViewModel = viewModel(),
    onBackToMain: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val cellSize = (configuration.screenWidthDp.dp - 32.dp) / 10

    var showSettings by remember { mutableStateOf(false) }
    var isBgmOn by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 점수 및 헤더
            GameInfoHeader(
                viewModel = viewModel,
                onShowSettings = {showSettings = true}
                )

            // 시간제한
            TimeProgressBar(viewModel = viewModel)

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f) // Box Comp가 부모의 나머지 모든 공간 차지
            ) {
                AppleGrid(viewModel, cellSize)
                DragSelectionBox(viewModel)
            }

            Spacer(Modifier.height(50.dp))

            GameSettingsDialog(
                showDialog = showSettings,
                onDismiss = { showSettings = false },
                isBgmOn = isBgmOn,
                onBgmToggle = { isBgmOn = it },
                showGoMainButton = true,
                showRestartButton = true,
                onGoMain = { /* 메인으로 이동 로직 */ },
                onRestartGame = { viewModel.restartGame() }
            )

            // 게임 오버 다이얼로그
            if (viewModel.appleGameState is AppleGameState.GameOver) {
                GameOverDialog(
                    score = (viewModel.appleGameState as AppleGameState.GameOver).score,
                    onRestart = { viewModel.restartGame() },
                    onMainMenu = onBackToMain
                )
            }
        }
    }
}