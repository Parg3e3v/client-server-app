package com.parg3v.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parg3v.client.ui.theme.ClientserverappTheme
import com.parg3v.client.view.ClientView
import com.parg3v.client.view.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClientserverappTheme {
                val viewModel: ClientViewModel = hiltViewModel()

                val ip by viewModel.ip.collectAsStateWithLifecycle()
                val port by viewModel.port.collectAsStateWithLifecycle()
                val clientStatus by viewModel.clientStatus.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClientView(
                        modifier = Modifier.padding(innerPadding),
                        ipProvider = { ip },
                        validateIp = viewModel::validateIp,
                        portProvider = { port },
                        validatePort = viewModel::validatePort,
                        clientStatusProvider = { clientStatus },
                        startClient = viewModel::startClient,
                        stopClient = viewModel::stopClient
                    )
                }
            }
        }
    }
}