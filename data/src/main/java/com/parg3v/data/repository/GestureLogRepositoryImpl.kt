package com.parg3v.data.repository

import android.util.Log
import com.parg3v.data.local.GestureLogDao
import com.parg3v.data.local.GestureLogEntity
import com.parg3v.data.mappers.toGestureLog
import com.parg3v.domain.model.GestureLog
import com.parg3v.domain.repository.GestureLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GestureLogRepositoryImpl @Inject constructor(private val gestureLogDao: GestureLogDao) :
    GestureLogRepository {

    override suspend fun logGesture(message: String) {
        withContext(Dispatchers.IO) {
            val log = GestureLogEntity(
                timestamp = System.currentTimeMillis(),
                message = message
            )
            gestureLogDao.insert(log)
            Log.d("WebSocketChat [SQLite]", "gesture is logged")
        }
    }

    override suspend fun getAllLogs(): List<GestureLog> {
        return withContext(Dispatchers.IO) {
            val logs = gestureLogDao.getAllLogs()
            Log.d("WebSocketChat [SQLite]", "getAllLogs: $logs")
            logs.map { it.toGestureLog() }
        }
    }

    override suspend fun clearAllLogs() {
        gestureLogDao.clearAllLogs()
        Log.d("WebSocketChat [SQLite]", "All logs cleared")
    }
}