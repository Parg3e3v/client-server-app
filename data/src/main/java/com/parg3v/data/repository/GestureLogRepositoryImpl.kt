package com.parg3v.data.repository

import com.parg3v.data.local.GestureLogDao
import com.parg3v.data.local.GestureLogEntity
import com.parg3v.data.mappers.toGestureLog
import com.parg3v.domain.model.GestureLog
import com.parg3v.domain.repository.GestureLogRepository
import javax.inject.Inject

class GestureLogRepositoryImpl @Inject constructor(private val gestureLogDao: GestureLogDao) :
    GestureLogRepository {

    override suspend fun logGesture(message: String) {
        val log = GestureLogEntity(
            timestamp = System.currentTimeMillis(),
            message = message
        )
        gestureLogDao.insert(log)
    }

    override fun getAllLogs(): List<GestureLog> {
        return gestureLogDao.getAllLogs().map { it.toGestureLog() }
    }
}