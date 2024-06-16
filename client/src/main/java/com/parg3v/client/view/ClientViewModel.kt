package com.parg3v.client.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parg3v.client.model.ClientStatus
import com.parg3v.domain.common.Result
import com.parg3v.domain.use_cases.client.GetIpClientAppUseCase
import com.parg3v.domain.use_cases.client.GetPortClientAppUseCase
import com.parg3v.domain.use_cases.client.SaveIpClientAppUseCase
import com.parg3v.domain.use_cases.client.SavePortClientAppUseCase
import com.parg3v.domain.use_cases.client.StartClientUseCase
import com.parg3v.domain.use_cases.client.StopClientUseCase
import com.parg3v.domain.use_cases.client.ValidateIpAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val startClientUseCase: StartClientUseCase,
    private val stopClientUseCase: StopClientUseCase,
    private val validateIpAddressUseCase: ValidateIpAddressUseCase,
    private val getIpClientAppUseCase: GetIpClientAppUseCase,
    private val saveIpClientAppUseCase: SaveIpClientAppUseCase,
    private val getPortClientAppUseCase: GetPortClientAppUseCase,
    private val savePortClientAppUseCase: SavePortClientAppUseCase
): ViewModel() {

    private val _ip = MutableStateFlow("")
    val ip: StateFlow<String> = _ip.asStateFlow()

    private val _port = MutableStateFlow("")
    val port: StateFlow<String> = _port.asStateFlow()

    private val _clientStatus = MutableStateFlow<ClientStatus>(ClientStatus.Offline)
    val clientStatus: StateFlow<ClientStatus> = _clientStatus.asStateFlow()

    init {
        getPort()
        getIp()
    }

    fun validatePort(port: String): Boolean {
        if (port.toIntOrNull() != null || port.isEmpty()) {
            if (port.length == 4) {
                _port.value = port
                savePort(port)
                return true
            }
        }
        return false
    }

    fun validateIp(ip: String): Boolean {
        if(validateIpAddressUseCase(ip)) {
            _ip.value = ip
            saveIp(ip)
            return true
        }
        return false
    }

    private fun getPort() {
        getPortClientAppUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _port.value = result.data ?: ""
                }
                else -> _port.value = ""
            }
        }.launchIn(viewModelScope)

    }

    private fun savePort(value: String) {
        savePortClientAppUseCase(
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

    private fun getIp() {
        getIpClientAppUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _ip.value = result.data ?: ""
                }
                else -> _ip.value = ""
            }
        }.launchIn(viewModelScope)

    }

    private fun saveIp(value: String) {
        saveIpClientAppUseCase(
            value = value
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _ip.value = ""
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun startClient() {
        viewModelScope.launch {
            try {
                _clientStatus.value = ClientStatus.Online
                startClientUseCase(_ip.value, _port.value.toInt())
            }  catch (e: Exception) {
                _clientStatus.value = ClientStatus.Error(e.localizedMessage ?: "Unknown error occurred")
                e.printStackTrace()
            } finally {
                _clientStatus.value = ClientStatus.Offline
            }
        }
    }

    fun stopClient(){
        viewModelScope.launch {
            stopClientUseCase()
            _clientStatus.value = ClientStatus.Offline
        }
    }
}