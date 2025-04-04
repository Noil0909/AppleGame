package com.example.applegame.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppleCell(
    value: Int,
    isSelected: Boolean,
    onSizeReady: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .padding(2.dp)
            .border(1.dp, Color.Gray)
            .background(if (isSelected) Color.Yellow else Color.White)
            .onGloballyPositioned {
                onSizeReady(it.size.width.toFloat())
            },
        contentAlignment = Alignment.Center
    ) {
        if (value > 0) {
            Text(text = value.toString(), color = Color.Red, fontSize = 16.sp)
        }
    }
}
