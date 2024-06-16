package com.parg3v.client.view

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.parg3v.client.MyAccessibilityService
import com.parg3v.client.R
import com.parg3v.client.components.CustomClientDialog
import com.parg3v.client.model.ClientStatus

@Composable
fun ClientView(
    modifier: Modifier = Modifier,
    ipProvider: () -> String,
    validateIp: (String) -> Boolean,
    portProvider: () -> String,
    validatePort: (String) -> Boolean,
    clientStatusProvider: () -> ClientStatus,
    stopClient: () -> Unit,
    startClient: () -> Unit
) {
    val buttonText: String
    val dialogVisible = remember { mutableStateOf(false) }
    val switchClientState: () -> Unit
    val buttonColor: Color
    val serviceError = stringResource(id = R.string.service_error)

    val context = LocalContext.current

    if (clientStatusProvider() is ClientStatus.Online) {
        switchClientState = stopClient
        buttonColor = MaterialTheme.colorScheme.error
        buttonText = stringResource(id = R.string.end)
    } else {
        switchClientState = startClient
        buttonColor = MaterialTheme.colorScheme.primary
        buttonText = stringResource(R.string.start)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_col_inner))
        ) {
            if (dialogVisible.value)
                CustomClientDialog(
                    onDismiss = { dialogVisible.value = false },
                    ipProvider = ipProvider,
                    portProvider = portProvider,
                    validatePort = validatePort,
                    validateIp = validateIp
                )

            Text(
                text = stringResource(id = R.string.client),
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = { dialogVisible.value = true }) {
                Text(text = stringResource(R.string.config))
            }

            Button(
                onClick = {
                    val service = MyAccessibilityService.getInstance()
                    if (service == null) {
                        Toast.makeText(
                            context,
                            serviceError,
                            Toast.LENGTH_LONG
                        ).show()
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    } else {
                        switchClientState()
                    }
                }, colors = ButtonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = buttonText)
            }

            var clientStatusText by remember { mutableStateOf("") }

            val clientStatus = clientStatusProvider()
            clientStatusText = when (clientStatus) {
                is ClientStatus.Error -> stringResource(
                    id = R.string.client_error,
                    clientStatus.message
                )

                is ClientStatus.Offline -> stringResource(id = R.string.client_offline)
                is ClientStatus.Online -> stringResource(
                    id = R.string.client_online,
                    ipProvider(),
                    portProvider()
                )

            }

            Text(
                text = clientStatusText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview
@Composable
private fun ClientViewPreview() {
    ClientView(
        ipProvider = { "" },
        validateIp = { true },
        portProvider = { "" },
        validatePort = { true },
        clientStatusProvider = { ClientStatus.Offline },
        stopClient = {},
        startClient = {})
}