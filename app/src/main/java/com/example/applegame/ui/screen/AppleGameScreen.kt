package com.example.applegame.ui.screen

import android.graphics.ColorFilter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.applegame.R
import com.example.applegame.domain.model.Apple
import com.example.applegame.domain.model.AppleGameState
import com.example.applegame.ui.component.GameOverDialog
import com.example.applegame.ui.viewmodel.AppleGameViewModel

@Composable
fun AppleGameScreen(
    viewModel: AppleGameViewModel = viewModel(),
    onBackToMain: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 점수
        GameInfoHeader(viewModel = viewModel)

        // 사과들
        AppleGrid(viewModel = viewModel)

        // 게임 오버 다이얼로그
        if (viewModel.appleGameState is AppleGameState.GameOver) {
            GameOverDialog(
                score = (viewModel.appleGameState as AppleGameState.GameOver).score,
                onRestart = { viewModel.restartGame() },
                onMainMenu = onBackToMain
            )
        }
    }
}

@Composable
private fun AppleGrid(
    viewModel: AppleGameViewModel,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemSize = (screenWidth - 32.dp) / 10

    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(viewModel.apples, key = { it.id }) { apple ->
            AppleItem(
                apple = apple,
                itemSize = itemSize,
                isSelected = viewModel.selectedIds.contains(apple.id),
                onClick = { viewModel.toggleApple(apple.id) }
            )
        }
    }
}


@Composable
private fun AppleItem(
    apple: Apple,
    itemSize: Dp,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedSize by animateDpAsState(
        targetValue = if (isSelected) itemSize * 1.2f else itemSize,
        animationSpec = spring(stiffness = 500f)
    )

    Card(
        modifier = Modifier
            .size(animatedSize)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier.background(Color.White)
        ) {
            Image(
                painter = painterResource(R.drawable.apple1_icon),
                contentDescription = null,
                colorFilter = if (isSelected) tint(Color.Yellow) else null
            )
            Text(
                text = "${apple.number}",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GameInfoHeader(
    viewModel: AppleGameViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "점수: ${viewModel.score}",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "남은 시간: ${viewModel.remainingTime}초",
            style = MaterialTheme.typography.titleLarge,
            color = if (viewModel.remainingTime <= 10) Color.Red else Color.Unspecified
        )
    }
}