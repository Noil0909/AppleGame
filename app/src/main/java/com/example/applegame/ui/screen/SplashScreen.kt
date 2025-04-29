//package com.example.applegame.ui.screen
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.LottieConstants
//import com.airbnb.lottie.compose.rememberLottieComposition
//import com.example.applegame.R
//import kotlinx.coroutines.delay
//
//@Composable
//fun SplashScreen(onNavigate: () -> Unit) {
//    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_apple_animation))
//
//    LaunchedEffect(Unit) {
//        delay(3000)
//        onNavigate()
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        contentAlignment = Alignment.Center
//    ){
//        LottieAnimation(
//            composition = lottieComposition,
//            iterations = LottieConstants.IterateForever,
//            modifier = Modifier
//                .size(250.dp)
//                .align(Alignment.Center)
//        )
//    }
//}