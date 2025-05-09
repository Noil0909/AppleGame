package com.noil.applegame.data.record

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRecordRepository(private val dao: GameRecordDao) {

    // 기록 저장
    suspend fun insertRecord(score: Int) {
        val record = GameRecord(
            score = score,
            timestamp = System.currentTimeMillis()
        )

        withContext(Dispatchers.IO) {
            dao.insertRecord(record)
        }
    }

    // 모든 기록 조회 (최신순)
    suspend fun getAllRecords(): List<GameRecord> {
        return withContext(Dispatchers.IO) {
            dao.getAllRecords()
        }
    }

    // 기록 삭제
    suspend fun deleteRecord(record: GameRecord) {
        withContext(Dispatchers.IO) {
            dao.deleteRecord(record)
        }
    }
}