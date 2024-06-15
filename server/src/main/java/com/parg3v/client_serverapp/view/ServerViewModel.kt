package com.parg3v.client_serverapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parg3v.client_serverapp.model.LogsStatus
import com.parg3v.client_serverapp.model.ServerStatus
import com.parg3v.domain.common.Result
import com.parg3v.domain.use_cases.GetLogsFormDBUseCase
import com.parg3v.domain.use_cases.ProvideServerIpUseCase
import com.parg3v.domain.use_cases.StartServerUseCase
import com.parg3v.domain.use_cases.StopServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase,
    private val provideServerIpUseCase: ProvideServerIpUseCase,
    private val getGestureLogsUseCase: GetLogsFormDBUseCase
) : ViewModel() {

    private val _port = MutableStateFlow("")
    val port: StateFlow<String> = _port.asStateFlow()

    private val _ip = MutableStateFlow("")
    val ip: StateFlow<String> = _ip.asStateFlow()

    private val _serverStatus = MutableStateFlow<ServerStatus>(ServerStatus.Offline)
    val serverStatus: StateFlow<ServerStatus> = _serverStatus.asStateFlow()

    private val _isServerStarted = MutableStateFlow(false)
    val isServerStarted: StateFlow<Boolean> = _isServerStarted.asStateFlow()

    private val _gestureLogs = MutableStateFlow(LogsStatus())
    val gestureLogs: StateFlow<LogsStatus> = _gestureLogs.asStateFlow()

    init {
        getServerIp()
        getLogs()
    }

    private fun getServerIp() {
        _ip.value = provideServerIpUseCase()
    }

    fun setPort(port: String) {
        if (port.toIntOrNull() != null || port.isEmpty())
            _port.value = port
    }

    fun startServer() {
        viewModelScope.launch {
            try {
                startServerUseCase(port.value.toInt())
                _serverStatus.value = ServerStatus.Online
                _isServerStarted.value = true
            } catch (e: Exception) {
                _serverStatus.value = ServerStatus.Error(e.localizedMessage ?: "")
                _isServerStarted.value = false
            }
        }
    }

    fun stopServer() {
        viewModelScope.launch {
            stopServerUseCase()
            _serverStatus.value = ServerStatus.Offline
            _isServerStarted.value = false
        }
    }

    fun getLogs(){
        getGestureLogsUseCase().onEach {result->
            when (result) {
                is Result.Error -> _gestureLogs.value =LogsStatus(error = result.error)
                is Result.Loading -> _gestureLogs.value =LogsStatus(isLoading = true)
                is Result.Success -> _gestureLogs.value =LogsStatus(data = result.data)
            }
        }
    }

}