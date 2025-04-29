package com.example.applegame

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.applegame.ui.common.BgmManager
import com.example.applegame.ui.navigation.AppNavigation
import com.example.applegame.ui.theme.AppleGameTheme
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        enableEdgeToEdge()

        BgmManager.startBgm(this, R.raw.applegame_bgm)

        setContent {
            AppleGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() // 네비게이션 컨트롤러 호출
                }
            }
        }
    }
    // 전체 화면 모드 설정
    private fun hideSystemBars() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun onPause() {
        super.onPause()
        BgmManager.pauseBgm()    }

    override fun onResume() {
        super.onResume()
        BgmManager.resumeBgm()
    }

    override fun onDestroy() {
        super.onDestroy()
        BgmManager.stopBgm()
    }
}

