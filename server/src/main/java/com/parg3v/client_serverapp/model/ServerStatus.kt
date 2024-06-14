package com.parg3v.client_serverapp.model

sealed class ServerStatus {
    data object Online : ServerStatus()
    data object Offline : ServerStatus()
    data class Error(val message: String) : ServerStatus()
}
