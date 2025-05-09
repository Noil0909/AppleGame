package com.noil.applegame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noil.applegame.ui.screen.AppleGameScreen
import com.noil.applegame.ui.screen.MainScreen
import com.noil.applegame.ui.screen.RecordScreen
//import com.example.applegame.ui.screen.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main" // 초기 화면 설정
    ) {
//        // 인트로 화면
//        composable("splash") {
//            SplashScreen(
//                onNavigate = {
//                    navController.navigate("main") {
//                        popUpTo("splash") { inclusive = true } // splash 화면 백 스택에서 제거
//                    }
//                }
//            )
//        }

        // 메인 화면
        composable(Screen.Main.name) {
            MainScreen(
                onNavigate = { screen ->
                    navController.navigate(screen.name)
                }
            )
        }

        // 게임 화면
        composable(Screen.AppleGame.name) {
            AppleGameScreen(
                onBackToMain = { navController.popBackStack() }
            )
        }

        // 기록 화면
        composable(Screen.Records.name) {
            RecordScreen(
               onBackToMain = { navController.popBackStack() }
            )
        }

    }
}