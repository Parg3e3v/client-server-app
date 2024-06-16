package com.parg3v.domain.repository

interface ServerRepository {
    suspend fun startServer(port: Int)
    suspend fun stopServer()
    fun sendGestures()
    fun generateRandomGesture(): String
    suspend fun handleBrowserOpen()
}