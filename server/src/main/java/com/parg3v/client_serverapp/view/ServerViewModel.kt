package com.parg3v.client_serverapp.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parg3v.client_serverapp.model.LogsStatus
import com.parg3v.client_serverapp.model.ServerStatus
import com.parg3v.domain.common.Result
import com.parg3v.domain.use_cases.server.GetLogsFormDBUseCase
import com.parg3v.domain.use_cases.common.ProvideServerIpUseCase
import com.parg3v.domain.use_cases.server.ClearLogsFormDBUseCase
import com.parg3v.domain.use_cases.server.GetPortServerAppUseCase
import com.parg3v.domain.use_cases.server.SavePortServerAppUseCase
import com.parg3v.domain.use_cases.server.StartServerUseCase
import com.parg3v.domain.use_cases.server.StopServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase,
    private val provideServerIpUseCase: ProvideServerIpUseCase,
    private val getGestureLogsUseCase: GetLogsFormDBUseCase,
    private val clearLogsFormDBUseCase: ClearLogsFormDBUseCase,
    private val getPortServerAppUseCase: GetPortServerAppUseCase,
    private val savePortServerAppUseCase: SavePortServerAppUseCase
) : ViewModel() {

    private val _port = MutableStateFlow("")
    val port: StateFlow<String> = _port.asStateFlow()

    private val _ip = MutableStateFlow("")
    val ip: StateFlow<String> = _ip.asStateFlow()

    private val _serverStatus = MutableStateFlow<ServerStatus>(ServerStatus.Offline)
    val serverStatus: StateFlow<ServerStatus> = _serverStatus.asStateFlow()

    private val _gestureLogs = MutableStateFlow(LogsStatus())
    val gestureLogs: StateFlow<LogsStatus> = _gestureLogs.asStateFlow()

    init {
        getServerIp()
        getPort()
    }

    private fun getPort() {
        getPortServerAppUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _port.value = result.data ?: ""
                }
                else -> _port.value = ""
            }
        }.launchIn(viewModelScope)

    }

    private fun savePort(value: String) {
        savePortServerAppUseCase(
            value = value
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _port.value = ""
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getServerIp() {
        _ip.value = provideServerIpUseCase()
    }

    fun validatePort(port: String) {
        if (port.toIntOrNull() != null || port.isEmpty()) {
            if (port.length <= 4) {
                _port.value = port
                savePort(port)
            }
        }
    }

    fun startServer() {
        viewModelScope.launch {
            try {
                startServerUseCase(_port.value.toInt())
                _serverStatus.value = ServerStatus.Online
            } catch (e: Exception) {
                _serverStatus.value = ServerStatus.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun stopServer() {
        viewModelScope.launch {
            stopServerUseCase()
            _serverStatus.value = ServerStatus.Offline
        }
    }

    fun getLogs() {
        Log.d("WebSocketChat [ViewModel]", "trying to get logs...")
        getGestureLogsUseCase().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _gestureLogs.value = LogsStatus(error = result.error)
                    Log.e("WebSocketChat [ViewModel]", "logs error: ${result.error}")
                }
                is Result.Loading -> {
                    _gestureLogs.value = LogsStatus(isLoading = true)
                    Log.d("WebSocketChat [ViewModel]", "loading...")
                }
                is Result.Success -> {
                    Log.d("WebSocketChat [ViewModel]", "logs got: ${result.data}")
                    _gestureLogs.value = LogsStatus(data = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clearLogs(){
        viewModelScope.launch {
            clearLogsFormDBUseCase()
            _gestureLogs.value = LogsStatus()
            getLogs()
        }
    }

}