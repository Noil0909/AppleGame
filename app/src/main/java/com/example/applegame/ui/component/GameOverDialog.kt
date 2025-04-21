package com.example.applegame.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameOverDialog(
    score: Int,
    onRestart: () -> Unit,
    onMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        shape = RoundedCornerShape(24.dp),
        containerColor = Color(0xFFFFF0F0),
        onDismissRequest = { /* 취소 불가 */ },
        modifier = modifier,
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "게임 오버",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color(0xFFFF6B6B)
                        color = Color(0xFF444444)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "점수",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = Color(0xFF444444)
                    )
                )
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 28.sp,
                        color = Color(0xFFFF6B6B)
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("다시하기")
                }

                Button(
                    onClick = onMainMenu,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("처음으로")
                }
            }
        },
    )
}