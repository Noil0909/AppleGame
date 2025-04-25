package com.example.applegame.data.record

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameRecordDao {

    @Insert
    suspend fun insertRecord(record: GameRecord)

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    suspend fun getAllRecords(): List<GameRecord>
}