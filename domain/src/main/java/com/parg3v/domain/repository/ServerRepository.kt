package com.parg3v.domain.repository

interface ServerRepository {
    suspend fun startServer(port: Int)
    fun stopServer()
    fun sendGestures()
    fun generateRandomGesture(): String
}