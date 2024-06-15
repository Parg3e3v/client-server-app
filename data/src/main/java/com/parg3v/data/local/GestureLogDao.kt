package com.parg3v.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GestureLogDao {
    @Insert
    suspend fun insert(log: GestureLogEntity)

    @Query("SELECT * FROM gesture_logs ORDER BY timestamp DESC")
    fun getAllLogs(): List<GestureLogEntity>
}