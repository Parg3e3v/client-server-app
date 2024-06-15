package com.parg3v.domain.repository

import com.parg3v.domain.model.GestureLog

interface GestureLogRepository {
    suspend fun logGesture(message: String)
    fun getAllLogs(): List<GestureLog>
}