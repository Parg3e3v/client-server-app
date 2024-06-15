package com.parg3v.client.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parg3v.client.model.ClientStatus
import com.parg3v.domain.use_cases.client.StartClientUseCase
import com.parg3v.domain.use_cases.client.StopClientUseCase
import com.parg3v.domain.use_cases.client.ValidateIpAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val startClientUseCase: StartClientUseCase,
    private val stopClientUseCase: StopClientUseCase,
    private val validateIpAddressUseCase: ValidateIpAddressUseCase,
): ViewModel() {
    private val _ip = MutableStateFlow("")
    val ip: StateFlow<String> = _ip.asStateFlow()

    private val _port = MutableStateFlow("")
    val port: StateFlow<String> = _port.asStateFlow()

    private val _clientStatus = MutableStateFlow<ClientStatus>(ClientStatus.Offline)
    val clientStatus: StateFlow<ClientStatus> = _clientStatus.asStateFlow()

    fun validatePort(port: String): Boolean {
        if (port.toIntOrNull() != null || port.isEmpty()) {
            if (port.length <= 5) {
                _port.value = port
                return true
            }
        }
        return false
    }

    fun validateIp(ip: String): Boolean {
        if(validateIpAddressUseCase(ip)) {
            _ip.value = ip
            return true
        }
        return false
    }
    fun startClient() {
        viewModelScope.launch {
            try {
                startClientUseCase(_ip.value, _port.value.toInt())
                _clientStatus.value = ClientStatus.Online
            }  catch (e: Exception) {
                _clientStatus.value = ClientStatus.Error(e.localizedMessage ?: "Unknown error occurred")
                e.printStackTrace()
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