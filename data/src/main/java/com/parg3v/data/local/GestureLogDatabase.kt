package com.parg3v.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GestureLogEntity::class], version = 1, exportSchema = false)
abstract class GestureLogDatabase : RoomDatabase() {
    abstract fun gestureLogDao(): GestureLogDao
}