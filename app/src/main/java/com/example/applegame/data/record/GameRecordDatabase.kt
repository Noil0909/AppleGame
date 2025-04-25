package com.example.applegame.data.record

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [GameRecord::class], // 엔티티 등록
    version = 1,
    exportSchema = false
)
abstract class GameRecordDatabase : RoomDatabase() {

    abstract fun gameRecordDao(): GameRecordDao

    companion object {
        @Volatile
        private var INSTANCE: GameRecordDatabase? = null

        fun getInstance(context: Context): GameRecordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameRecordDatabase::class.java,
                    "game_record_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}