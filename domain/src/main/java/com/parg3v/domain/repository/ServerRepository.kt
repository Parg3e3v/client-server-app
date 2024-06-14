package com.parg3v.domain.repository

interface ServerRepository {
    fun startServer(port: Int)
    fun stopServer()
}