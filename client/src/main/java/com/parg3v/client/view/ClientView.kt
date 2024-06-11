package com.parg3v.client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.parg3v.client.R
import com.parg3v.client.components.CustomClientDialog

@Composable
fun ClientView(modifier: Modifier = Modifier) {
    val start = stringResource(R.string.start)
    val end = stringResource(id = R.string.end)
    var started by remember { mutableStateOf(true) }
    val dialogVisible = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { dialogVisible.value = true }) {
            Text(text = stringResource(R.string.config))
        }

        if (dialogVisible.value)
            CustomClientDialog(
                onDismiss = { dialogVisible.value = false },
                ipProvider = { "id" },
                onIpChange = {},
                portProvider = { "port" },
                onPortChange = {}
            )

        Button(
            onClick = { started = !started }, colors = ButtonColors(
                containerColor = if (started) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = if (started) start else end)
        }
    }
}


@Preview
@Composable
private fun ClientViewPreview() {
    ClientView()
}