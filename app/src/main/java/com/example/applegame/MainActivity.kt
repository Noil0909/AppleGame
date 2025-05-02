package com.example.applegame

import android.content.Context
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
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.applegame.common.BgmManager
import com.example.applegame.common.SettingsRepository
import com.example.applegame.ui.navigation.AppNavigation
import com.example.applegame.ui.theme.AppleGameTheme
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 앱 생명주기 옵저버 등록
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))

        // 설정 불러오기
        SettingsRepository.init(this)

        BgmManager.initializeFromPrefs(this)

        // 전체화면
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        enableEdgeToEdge()

        // 최초 앱 시작 시 BGM 시작
        if (BgmManager.isBgmOn) {
            BgmManager.startBgm(this, R.raw.applegame_bgm)
        }

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
}

class AppLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        // 앱이 포그라운드로 복귀했을 때 BGM 재생
        if (BgmManager.isBgmOn) {
            BgmManager.startBgm(context, R.raw.applegame_bgm)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        // 앱이 백그라운드로 전환되면 일시정지
        BgmManager.pauseBgm()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        BgmManager.stopBgm()
    }
}
