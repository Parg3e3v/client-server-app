package com.parg3v.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gesture_logs")
data class GestureLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val message: String
)