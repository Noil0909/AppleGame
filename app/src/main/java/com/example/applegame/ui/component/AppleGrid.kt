package com.example.applegame.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applegame.R
import com.example.applegame.domain.model.Apple
import com.example.applegame.ui.viewmodel.AppleGameViewModel

@Composable
fun AppleGrid(
    viewModel: AppleGameViewModel,
    cellSize: Dp
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(9),
        modifier = Modifier
            .padding(top=50.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
    ) {
        items(viewModel.apples.sortedBy { it.position }) { apple ->
            if (apple.number != 0) {
                AppleItem(
                    apple = apple,
                    isSelected = viewModel.selectedIds.contains(apple.id),
                    cellSize = cellSize
                )
            }
        }
    }
}

@Composable
fun AppleItem(
    apple: Apple,
    isSelected: Boolean,
    cellSize: Dp
) {
    Box(
        modifier = Modifier
            .size(cellSize * 0.9f)
            .padding(1.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.apple2_icon),
            contentDescription = "사과",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = if (isSelected) 1.2f else 1f
                    scaleY = if (isSelected) 1.2f else 1f
                },
            colorFilter = if (isSelected) tint(Color.Red) else null,
            contentScale = ContentScale.Inside
        )
        Text(
            text = "${apple.number}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y= 3.dp)
                .align(Alignment.Center)
        )
    }
}
