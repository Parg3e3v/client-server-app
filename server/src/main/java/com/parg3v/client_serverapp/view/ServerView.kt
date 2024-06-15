package com.parg3v.client_serverapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.parg3v.client_serverapp.R
import com.parg3v.client_serverapp.components.CustomServerDialog
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
    isServerStarted: () -> Boolean,
    serverStatusProvider: () -> ServerStatus,
    logStatus: () -> LogsStatus,
    getLogs: () -> Unit
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
                getLogs()
                ShowLogsDialog(logsVisible, logStatus())
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
                    enabled = !isServerStarted()
                ) {
                    Text(text = stringResource(R.string.start))
                }

                Button(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        stopServer()
                    },
                    enabled = isServerStarted()
                ) {
                    Text(text = stringResource(id = R.string.stop))
                }
            }


            Button(onClick = { logsVisible.value = true }) {
                Text(text = stringResource(R.string.logs))
            }

            var serverStatusText by remember { mutableStateOf("") }

            val serverStatus = serverStatusProvider()
            serverStatusText = when (serverStatus) {
                is ServerStatus.Error -> stringResource(id = R.string.server_error, serverStatus.message)
                is ServerStatus.Offline -> stringResource(id = R.string.server_offline)
                is ServerStatus.Online -> stringResource(id = R.string.server_online, ipProvider(), portProvider())

            }

            Text(text = serverStatusText, style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Composable
fun ShowLogsDialog(logsVisible: MutableState<Boolean>, logStatus: LogsStatus) {

    Dialog(onDismissRequest = { logsVisible.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.logs_dialog_corner_radius)),
        ) {
            LazyColumn {
                logStatus.data?.let { logList ->
                    items(logList) { log ->
                        Text(
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                            text = "${log.timestamp} - ${log.message}"
                        )
                    }
                } ?: run {
                    item {
                        Text(
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                            text = stringResource(id = R.string.no_logs)
                        )
                    }
                }

            }
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
        isServerStarted = { false },
        serverStatusProvider = { ServerStatus.Offline },
        logStatus = { LogsStatus(data = listOf(GestureLog(timestamp = 654654, message = "test"))) },
        getLogs = {}
    )
}