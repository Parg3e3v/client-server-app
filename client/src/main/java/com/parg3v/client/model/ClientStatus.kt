package com.parg3v.client.model

sealed class ClientStatus(isStarted: Boolean) {
    data object Online: ClientStatus(true)
    data object Offline: ClientStatus(false)
    data class Error(val message: String): ClientStatus(false)
}