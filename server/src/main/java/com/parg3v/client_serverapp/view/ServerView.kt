package com.parg3v.client_serverapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.parg3v.client_serverapp.R
import com.parg3v.client_serverapp.components.CustomServerDialog
import com.parg3v.client_serverapp.components.LogsDialog
import com.parg3v.client_serverapp.model.LogsStatus
import com.parg3v.client_serverapp.model.ServerStatus
import com.parg3v.domain.model.GestureLog

@Composable
fun ServerView(
    modifier: Modifier = Modifier,
    portProvider: () -> String,
    onPortChange: (String) -> Unit,
    ipProvider: () -> String,
    startServer: () -> Unit,
    stopServer: () -> Unit,
    serverStatusProvider: () -> ServerStatus,
    logStatus: () -> LogsStatus,
    getLogs: () -> Unit,
    cleanLogs: () -> Unit
) {

    var dialogVisible by remember { mutableStateOf(false) }
    val logsVisible = remember { mutableStateOf(false) }


    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_col_inner))
        ) {

            if (dialogVisible)
                CustomServerDialog(
                    onDismiss = { dialogVisible = false },
                    portProvider = portProvider,
                    onPortChange = onPortChange,
                    ipProvider = ipProvider
                )

            if (logsVisible.value) {
                LogsDialog(logsVisible, logStatus(), cleanLogs)
            }

            Text(
                text = stringResource(id = R.string.server),
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = { dialogVisible = true }) {
                Text(text = stringResource(R.string.config))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_row)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_row_inner))
            ) {
                Button(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        startServer()
                    },
                    enabled = !serverStatusProvider().isStarted
                ) {
                    Text(text = stringResource(R.string.start))
                }

                Button(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        stopServer()
                    },
                    enabled = serverStatusProvider().isStarted
                ) {
                    Text(text = stringResource(id = R.string.stop))
                }
            }


            Button(onClick = {
                getLogs()
                logsVisible.value = true
            }) {
                Text(text = stringResource(R.string.logs))
            }

            var serverStatusText by remember { mutableStateOf("") }

            val serverStatus = serverStatusProvider()
            serverStatusText = when (serverStatus) {
                is ServerStatus.Error -> stringResource(
                    id = R.string.server_error,
                    serverStatus.message
                )

                is ServerStatus.Offline -> stringResource(id = R.string.server_offline)
                is ServerStatus.Online -> stringResource(
                    id = R.string.server_online,
                    ipProvider(),
                    portProvider()
                )

            }

            Text(
                text = serverStatusText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}

@Preview
@Composable
private fun ServerViewPreview() {
    ServerView(
        portProvider = { "123" },
        onPortChange = {},
        ipProvider = { "127.0.0.1" },
        startServer = {},
        stopServer = {},
        serverStatusProvider = { ServerStatus.Offline },
        logStatus = { LogsStatus(data = listOf(GestureLog(timestamp = 654654, message = "test"))) },
        getLogs = {},
        cleanLogs = {}
    )
}