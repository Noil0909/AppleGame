package com.noil.applegame.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noil.applegame.ui.viewmodel.AppleGameViewModel


@Composable
fun TimeProgressBar(viewModel: AppleGameViewModel) {
    val remaining = viewModel.remainingTime
    val total = 120f // 수정필요 총 시간 (초)

    val targetProgress = remaining / total
    val animatedProgress = remember { Animatable(targetProgress) }

    LaunchedEffect(targetProgress) {
        if (targetProgress == 1f) {
            animatedProgress.snapTo(1f) // 즉시 채우기
        } else {
            animatedProgress.animateTo(
                targetValue = targetProgress,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
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
            size = Size(size.width * animatedProgress.value, size.height),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }
}