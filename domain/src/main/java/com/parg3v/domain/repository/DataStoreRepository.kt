package com.parg3v.domain.repository

interface DataStoreRepository {
    suspend fun saveIp(value: String)
    suspend fun getIp(): String?
    suspend fun savePortClient(value: String)
    suspend fun getPortClient(): String?
    suspend fun savePortServer(value: String)
    suspend fun getPortServer(): String?
}