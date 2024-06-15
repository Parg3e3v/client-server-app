package com.parg3v.client_serverapp.model

sealed class ServerStatus(val isStarted: Boolean) {
    data object Online : ServerStatus(true)
    data object Offline : ServerStatus(false)
    data class Error(val message: String) : ServerStatus(false)
}
