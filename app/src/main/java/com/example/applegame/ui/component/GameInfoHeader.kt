package com.example.applegame.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.applegame.R
import com.example.applegame.ui.viewmodel.AppleGameViewModel


@Composable
fun GameInfoHeader(
    viewModel: AppleGameViewModel,
    onShowSettings: () -> Unit
) {
    val jalnanFont = FontFamily(
        Font(R.font.jalnan2)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 10.dp, end = 10.dp)
            .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 남은 시간
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            contentAlignment = Alignment.CenterStart,
        ){
            Text(
                text = "${viewModel.remainingTime}초",
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = jalnanFont),
                color = Color(0xFF4CAF50)
            )
        }

        // 점수
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "${viewModel.score.value}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFFFF6B6B),
                        fontFamily = jalnanFont
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // 환경 설정
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ){
            IconButton(onClick = onShowSettings){
                Icon(
                    painter = painterResource(R.drawable.setting_icon),
                    contentDescription = "환경설정",
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}