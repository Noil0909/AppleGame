package com.example.applegame.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applegame.R
import com.example.applegame.common.BgmManager
import com.example.applegame.common.SettingsRepository
import com.example.applegame.common.SoundEffectManager
import com.example.applegame.common.SoundEffectManager.isSoundOn
import com.example.applegame.common.VibrationManager
import com.example.applegame.common.VibrationManager.isVibrationOn
import com.example.applegame.ui.component.GameSettingsDialog
import com.example.applegame.ui.navigation.Screen

@Composable
fun MainScreen( onNavigate: (Screen) -> Unit) {
    val context = LocalContext.current

    var showSettings by remember { mutableStateOf(false) }

    var isBgmOn by rememberSaveable { mutableStateOf(SettingsRepository.isBgmOn) }
    var isSoundOn by rememberSaveable { mutableStateOf(SettingsRepository.isSoundOn) }
    var isVibrationOn by rememberSaveable { mutableStateOf(SettingsRepository.isVibrationOn) }

    LaunchedEffect(Unit) {
        SettingsRepository.init(context)

        BgmManager.initializeFromPrefs(context)
        SoundEffectManager.initializeFromPrefs(context)
        VibrationManager.initializeFromPrefs(context)

        if (BgmManager.isBgmOn) {
            BgmManager.startBgm(context, R.raw.applegame_bgm)
        }

        SoundEffectManager.init(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F0)), // 연한 분홍 배경
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        IconButton(
            onClick = {showSettings = true },
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(R.drawable.setting_icon),
                contentDescription = "설정",
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(32.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "사과",
                color = Color(0xFFFF4444),
                fontSize = 80.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "게임",
                color = Color(0xFF4CAF50),
                fontSize = 80.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }

        Spacer(modifier = Modifier.height(50.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf(1,2,3,4,5,6,7,8,9)){number ->
                Box(
                    modifier = Modifier
                        .size(80.dp), // 크기 축소
                    contentAlignment = Center
                ){
                    // 사과 이미지 배경
                    Image(
                        painter = painterResource(R.drawable.apple2_icon),
                        contentDescription = "사과 배경",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                    // 숫자 표시
                    Text(
                        text = "$number",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.offset(y = 5.dp) // 숫자 위치 미세 조정
                    )
                }
            }
        }

        Row(
            modifier = Modifier
            .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            val interactionSourceStartButton = remember { MutableInteractionSource() }
            val interactionSourceRecordButton = remember { MutableInteractionSource() }
            val pressedStartButton by interactionSourceStartButton.collectIsPressedAsState()
            val pressedRecordButton by interactionSourceRecordButton.collectIsPressedAsState()

            val StartbuttonSize by animateDpAsState(
                targetValue = if (pressedStartButton) 190.dp else 180.dp,
                animationSpec = tween(durationMillis = 100),
                label = "StartbuttonSize"
            )

            val RecordButtonSize by animateDpAsState(
                targetValue = if (pressedRecordButton) 190.dp else 180.dp,
                animationSpec = tween(durationMillis = 100),
                label = "RecordbuttonSize"
            )

            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSourceStartButton,
                        indication = null,
                        onClick = {onNavigate(Screen.AppleGame)}  // 게임 화면으로 이동
                    )
                    .animateContentSize()
            ){
                Image(
                    painter = painterResource(R.drawable.red_apple),
                    contentDescription = "시작하기",
                    modifier = Modifier
                        .size(StartbuttonSize)
                        .padding(bottom = 70.dp)
                )
                Text(
                    text = "시작하기",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-20).dp)
                )
            }
            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSourceRecordButton,
                        indication = null,
                        onClick = {onNavigate(Screen.Records) }  // 기록보기 화면으로 이동
                    )
                    .animateContentSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.green_apple),
                    contentDescription = "기록보기",
                    modifier = Modifier
                        .size(RecordButtonSize)
                        .padding(bottom = 70.dp)
                )
                Text(
                    text = "기록보기",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-20).dp)
                )
            }
        }

        // 설정 다이얼로그
        GameSettingsDialog(
            showDialog = showSettings,
            onDismiss = { showSettings = false },
            isBgmOn = isBgmOn,
            onBgmToggle = {
                isBgmOn = it
                BgmManager.isBgmOn = it
                SettingsRepository.isBgmOn = it
                if (it) BgmManager.startBgm(context, R.raw.applegame_bgm) else BgmManager.stopBgm()
            },
            isSoundOn = isSoundOn,
            onSoundToggle = {
                isSoundOn = it
                SoundEffectManager.isSoundOn = it
                SettingsRepository.isSoundOn = it
            },
            isVibrationOn = isVibrationOn,

            onVibrationToggle = {
                isVibrationOn = it
                VibrationManager.isVibrationOn = it
                SettingsRepository.isVibrationOn = it
            },
            // 여기서는 showGoMainButton, showRestartButton 기본값 false 이므로 버튼 표시 X
        )
    }
}