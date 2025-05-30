package com.noil.applegame.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.noil.applegame.R
import com.noil.applegame.ui.screen.jalNanFont

@Composable
fun GameSettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    isBgmOn: Boolean,
    onBgmToggle: (Boolean) -> Unit,
    isSoundOn: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    isVibrationOn: Boolean,
    onVibrationToggle: (Boolean) -> Unit,

    // 세팅 dialog 재사용
    showGoMainButton: Boolean = false,
    showRestartButton: Boolean = false,
    onMainMenu: () -> Unit = {},
    onRestart: () -> Unit = {},
) {
    val jalnanFont = FontFamily(Font(R.font.jalnan2))

    if(!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text="게임설정",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = jalnanFont
                )
            )},
        text = {
            Column {
                SettingRow(icon = R.drawable.bgm_icon, label = "BGM", checked = isBgmOn, onCheckedChange = onBgmToggle)
                SettingRow(icon = R.drawable.sound_effect_icon, label = "효과음", checked = isSoundOn, onCheckedChange = onSoundToggle)
                SettingRow(icon = R.drawable.vibration_icon, label = "진동", checked = isVibrationOn, onCheckedChange = onVibrationToggle)
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
                        Text(
                            text = "다시하기",
                            style = TextStyle(fontFamily = jalnanFont)
                        )
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
                        Text(
                            text = "처음으로",
                            style = TextStyle(fontFamily = jalnanFont)
                            )
                    }
                }
            }
        },
        textContentColor = Color.Black
    )
}

@Composable
fun SettingRow(icon: Int, label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color(0xFFFF6B6B),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = TextStyle(fontFamily = jalNanFont),
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}