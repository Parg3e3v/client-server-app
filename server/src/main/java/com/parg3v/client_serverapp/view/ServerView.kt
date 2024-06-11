package com.parg3v.client_serverapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.parg3v.client_serverapp.R
import com.parg3v.client_serverapp.components.CustomServerDialog

@Composable
fun ServerView(modifier: Modifier = Modifier) {

    val dialogVisible = remember { mutableStateOf(false) }
    val logsVisible = remember { mutableStateOf(false) }

    var started by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { dialogVisible.value = true }) {
            Text(text = stringResource(R.string.config))
        }

        if (dialogVisible.value)
            CustomServerDialog(
                onDismiss = { dialogVisible.value = false },
                portProvider = { "port" },
                onPortChange = {}
            )

        if (logsVisible.value)
            Dialog(onDismissRequest = { logsVisible.value = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.logs_dialog_corner_radius)),
                ) {
                    Text(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.logs_dialog_padding)),
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                                "industry. Lorem Ipsum has been the industry's standard dummy text ever " +
                                "since the 1500s, when an unknown printer took a galley of type and scrambled " +
                                "it to make a type specimen book. It has survived not only five centuries, " +
                                "but also the leap into electronic typesetting, remaining essentially " +
                                "unchanged. It was popularised in the 1960s with the release of Letraset " +
                                "sheets containing Lorem Ipsum passages, and more recently with desktop " +
                                "publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                    )
                }
            }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { started = !started }, enabled = !started) {
                Text(text = stringResource(R.string.start))
            }


            Button(onClick = { started = !started }, enabled = started) {
                Text(text = stringResource(id = R.string.stop))
            }
        }

        Button(onClick = { logsVisible.value = true }) {
            Text(text = stringResource(R.string.logs))
        }

    }
}

@Preview
@Composable
private fun ServerViewPreview() {
    ServerView()
}