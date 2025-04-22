package com.example.applegame.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.applegame.R

@Composable
fun GameSettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    isBgmOn: Boolean,
    onBgmToggle: (Boolean) -> Unit,
    // 세팅 dialog 재사용
    showGoMainButton: Boolean = false,
    showRestartButton: Boolean = false,
    onMainMenu: () -> Unit = {},
    onRestart: () -> Unit = {},
) {
    if(!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("게임설정", style = MaterialTheme.typography.titleLarge)},
        text = {
            Column{
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(R.drawable.bgm_icon),
                        contentDescription = "BGM Icon",
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Switch(
                        checked = isBgmOn,
                        onCheckedChange = onBgmToggle
                    )
                }

            }
        },
        confirmButton = {
            if (showRestartButton){
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {
                            onDismiss()
                            onRestart()
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B6B)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("다시하기")
                    }

                    Button(
                        onClick = {
                                onDismiss()
                                onMainMenu()
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("처음으로")
                    }
                }
            }
        },
        textContentColor = Color.Black
    )

}