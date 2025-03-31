package com.example.applegame.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.applegame.ui.viewmodel.AppleGameViewModel


@Composable
fun TimeProgressBar(viewModel: AppleGameViewModel) {
    // targetProgress는 남은 시간을 0~1 사이의 값으로 변환
    val targetProgress = viewModel.remainingTime / 120f

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    LinearProgressIndicator(
        progress = animatedProgress,
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