package com.example.applegame.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.applegame.ui.viewmodel.AppleGameViewModel


@Composable
fun TimeProgressBar(viewModel: AppleGameViewModel) {
    val progress = viewModel.remainingTime / 120f // 120초 기준

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
