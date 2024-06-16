package com.parg3v.client_serverapp

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
import com.parg3v.client_serverapp.ui.theme.ClientserverappTheme
import com.parg3v.client_serverapp.view.ServerView
import com.parg3v.client_serverapp.view.ServerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClientserverappTheme {
                val viewModel: ServerViewModel = hiltViewModel()

                val port by viewModel.port.collectAsStateWithLifecycle()
                val ip by viewModel.ip.collectAsStateWithLifecycle()
                val serverStatus by viewModel.serverStatus.collectAsStateWithLifecycle()
                val logStatus by viewModel.gestureLogs.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ServerView(
                        modifier = Modifier.padding(innerPadding),
                        portProvider = { port },
                        onPortChange = viewModel::validatePort,
                        ipProvider = { ip },
                        startServer = viewModel::startServer,
                        stopServer = viewModel::stopServer,
                        serverStatusProvider = { serverStatus },
                        logStatus = { logStatus },
                        getLogs = viewModel::getLogs,
                        cleanLogs = viewModel::clearLogs
                    )
                }
            }
        }
    }
}