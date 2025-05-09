package com.noil.applegame.data.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameRecordDao {

    @Insert
    suspend fun insertRecord(record: GameRecord)

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    suspend fun getAllRecords(): List<GameRecord>

    @Delete
    suspend fun deleteRecord(record: GameRecord)
}