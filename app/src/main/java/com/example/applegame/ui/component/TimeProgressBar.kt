package com.example.applegame.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.viewmodel.AppleGameViewModel
import kotlinx.coroutines.delay
import kotlin.math.max



@Composable
fun TimeProgressBar(viewModel: AppleGameViewModel) {
    val totalTime = 10_000L // 다이어로그 수정중
    val gameState = viewModel.appleGameState
    val progress = remember { Animatable(1f) }

    LaunchedEffect(gameState) {
        if (gameState is AppleGameState.Playing) {
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = totalTime.toInt(), easing = LinearEasing)
            )
            viewModel.triggerGameOver() //애니메이션이 끝났을 때 GameOver
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(horizontal = 12.dp)
    ) {
        // 배경
        drawRoundRect(
            color = Color(0xFFC8E6C9),
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(8f, 8f)
        )
        // 진행 바
        drawRoundRect(
            color = Color(0xFF4CAF50),
            size = Size(size.width * progress.value, size.height),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }
}