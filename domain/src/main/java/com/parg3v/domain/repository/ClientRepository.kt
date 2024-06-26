package com.parg3v.domain.repository

interface ClientRepository {
    suspend fun startClient(ip: String, port: Int)
    fun stopClient()
    suspend fun performGesture(gesture: String)
}