package com.example.applegame.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.applegame.R
import com.example.applegame.ui.viewmodel.AppleGameViewModel


@Composable
fun GameInfoHeader(
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
            color = Color(0xFF4CAF50)
        )
    }
}