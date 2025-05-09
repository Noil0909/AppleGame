package com.noil.applegame.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noil.applegame.R

@Composable
fun RecordScreenHeader(
    onNavigateToMain: () -> Unit
) {
    val jalnanFont = FontFamily(Font(R.font.jalnan2))

    var isNavigating by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        // 왼쪽 상단에 back 버튼
        IconButton(
            onClick = {
                if (!isNavigating) {
                    isNavigating = true
                    onNavigateToMain()
                }
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black
            )
        }

        // 가운데에 기록보기 텍스트
        Text(
            text = "기록보기",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontFamily = jalnanFont,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}