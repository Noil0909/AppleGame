package com.example.applegame.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applegame.R
import com.example.applegame.data.record.GameRecord
import com.example.applegame.data.record.GameRecordDatabase
import com.example.applegame.data.record.GameRecordRepository
import com.example.applegame.ui.component.RecordScreenHeader
import com.example.applegame.ui.viewmodel.GameRecordViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val jalnanFont = FontFamily(Font(R.font.jalnan2))
@Composable
fun RecordScreen(
    onBackToMain: () -> Unit,
    context: Context = LocalContext.current
) {
    val db = remember { GameRecordDatabase.getInstance(context) }
    val repository = remember { GameRecordRepository(db.gameRecordDao()) }
    val viewModel = remember { GameRecordViewModel(repository) }

    val records by viewModel.recordList.collectAsState()
    val recordToDelete by viewModel.recordToDelete.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecords()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 15.dp)
        ) {
            RecordScreenHeader(onBackToMain)

            Spacer(modifier = Modifier.height(20.dp))

            if (records.isNotEmpty()) {
                RecordTable(records, viewModel)
            } else {
                EmptyRecordView()
            }
        }

        // 삭제 다이얼로그
        if (recordToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.cancelDeleteRecord() },
                title = { Text("기록 삭제", fontFamily = jalNanFont) },
                text = { Text("정말 이 기록을 삭제하시겠습니까?", fontFamily = jalNanFont) },
                confirmButton = {
                    Button(onClick = { viewModel.confirmDeleteRecord() }) {
                        Text("삭제", fontFamily = jalNanFont)
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.cancelDeleteRecord() }) {
                        Text("취소", fontFamily = jalNanFont)
                    }
                }
            )
        }
    }
}

@Composable
fun RecordTable(records: List<GameRecord>, viewModel: GameRecordViewModel) {
    val currentPage by viewModel.currentPage.collectAsState()
    val currentPageRange by viewModel.currentPageRange.collectAsState()
    val pageSize = 20

    val sortedByScore = records.sortedByDescending { it.score }
    val top3Records = sortedByScore.take(3)
    val latestRecords = records.sortedByDescending { it.timestamp }
        .filter { it !in top3Records }

    val pagedRecords = latestRecords
        .drop((currentPage - 1) * pageSize)
        .take(pageSize)


    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                RecordTableHeader()
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 1페이지에서만 상위 3개 표시
            if (currentPage == 1) {
                itemsIndexed(top3Records) { index, record ->
                    RecordTableRow(
                        record = record,
                        rank = index + 1,
                        isTopRank = true,
                        onDeleteClick = { viewModel.requestDeleteRecord(it) }
                    )
                }
            }

            // 현재 페이지의 일반 기록
            items(pagedRecords) { record ->
                RecordTableRow(
                    record = record,
                    rank = null,
                    isTopRank = false,
                    onDeleteClick = { viewModel.requestDeleteRecord(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 페이지 네비게이션
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // ◀ 이전 그룹
            if (currentPageRange.first > 1) {
                IconButton(onClick = { viewModel.goToPrevPageGroup() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = "이전"
                    )
                }
            }

            // 페이지 번호
            for (page in currentPageRange) {
                Button(
                    onClick = { viewModel.setPage(page) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (page == currentPage) Color(0xFFFF6B6B) else Color.White
                    ),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp),
                    contentPadding = PaddingValues(4.dp),
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = page.toString(),
                        fontSize = 12.sp,
                        fontFamily = jalNanFont,
                        color = if (page == currentPage) Color.White else Color.Black
                    )
                }
            }

            // ▶ 다음 그룹
            if (currentPageRange.last < viewModel.totalPages) {
                IconButton(onClick = { viewModel.goToNextPageGroup() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.page_forward_icon),
                        contentDescription = "다음"
                    )
                }
            }
        }
    }
}
@Composable
fun RecordTableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "",
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )
        Text(
            text = "점수",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = jalNanFont,
            fontSize = 18.sp,
            color = Color(0xFF4CAF50)
        )
        Text(
            text = "날짜",
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = jalNanFont,
            fontSize = 18.sp,
            color = Color(0xFF4CAF50)
        )

        Text(
            text = "삭제",
            modifier = Modifier.weight(0.75f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = jalNanFont,
            fontSize = 18.sp,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun RecordTableRow(
    record: GameRecord,
    rank: Int?,             // 몇 등이냐 (1,2,3만 메달)
    isTopRank: Boolean,     // 1~3등 구분
    onDeleteClick: (GameRecord) -> Unit
) {
    val dateFormat = remember {
        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    }
    val formattedDate = dateFormat.format(Date(record.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.5f)
        ) {
            if (isTopRank) {
                // 1,2,3등이면 메달 표시
                val medalid = when (rank) {
                    1 -> R.drawable.apple_gold_medal
                    2 -> R.drawable.apple_silver_medal
                    3 -> R.drawable.apple_bronze_medal
                    else -> R.drawable.green_apple
                }

                Image(
                    painter = painterResource(id = medalid),
                    contentDescription = "Top Rank Apple Icon",
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.CenterStart)
                )
            } else {
                // 나머지는 초록 사과 이미지
                Image(
                    painter = painterResource(id = R.drawable.green_apple),
                    contentDescription = "Record Icon",
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        Text(
            text = "${record.score}점",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontFamily = jalNanFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = formattedDate,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center,
            fontFamily = jalNanFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )

        Box(
            modifier = Modifier.weight(0.75f)
        ) {
            IconButton(
                onClick = { onDeleteClick(record) },
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "삭제",
                    tint = Color(0xFFFF6B6B)
                )
            }
        }
    }
}

@Composable
fun EmptyRecordView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.red_apple),
            contentDescription = "Empty",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "아직 저장된 기록이 없어요",
            fontSize = 18.sp,
            fontFamily = jalNanFont,
            color = Color.Gray
        )
    }
}