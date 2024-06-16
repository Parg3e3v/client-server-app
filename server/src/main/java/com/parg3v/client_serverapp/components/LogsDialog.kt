package com.parg3v.client_serverapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.window.Dialog
import com.parg3v.client_serverapp.R
import com.parg3v.client_serverapp.model.LogsStatus

@Composable
fun LogsDialog(
    logsVisible: MutableState<Boolean>,
    logStatus: LogsStatus,
    cleanLogs: () -> Unit
) {

    Dialog(onDismissRequest = { logsVisible.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.logs_dialog_corner_radius)),
        ) {
            logStatus.data?.let { logList ->
                if (logList.isNotEmpty()) {
                    TextButton(
                        onClick = { cleanLogs() },
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_clean_logs_button))
                    ) {
                        Text(
                            text = stringResource(id = R.string.clean_logs),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                else {
                    Text(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                        text = stringResource(id = R.string.no_logs)
                    )
                }
                LazyColumn {
                    items(logList.reversed()) { log ->
                        Text(
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                            text = "${log.timestamp} - ${log.message}"
                        )
                    }
                }
            } ?: run {
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                    text = stringResource(id = R.string.no_logs)
                )
            }
        }
    }
}