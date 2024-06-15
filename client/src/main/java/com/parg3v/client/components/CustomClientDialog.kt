package com.parg3v.client.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.parg3v.client.R

@Composable
fun CustomClientDialog(
    onDismiss: () -> Unit,
    ipProvider: () -> String,
    portProvider: () -> String,
    validateIp: (String) -> Boolean,
    validatePort: (String) -> Boolean
) {
    var ipText by remember { mutableStateOf(ipProvider()) }
    var portText by remember { mutableStateOf(portProvider()) }

    val context = LocalContext.current

    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(dimensionResource(id = R.dimen.padding_dialog_col))) {
                    Text(text = stringResource(R.string.title))
                    Spacer(Modifier.size(dimensionResource(id = R.dimen.dialog_spacer_1)))

                    OutlinedTextField(value = ipText, onValueChange = { ipText = it })

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.dialog_default_padding)))

                    OutlinedTextField(value = portText, onValueChange = { portText = it })
                }
                Spacer(Modifier.size(dimensionResource(id = R.dimen.dialog_default_padding)))
                Row(
                    Modifier
                        .padding(dimensionResource(id = R.dimen.dialog_default_padding))
                        .fillMaxWidth(),
                    Arrangement.spacedBy(
                        dimensionResource(id = R.dimen.dialog_default_padding),
                        Alignment.End
                    ),
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.deny))
                    }
                    Button(onClick = {
                        if( validateIp(ipText) && validatePort(portText))
                            onDismiss()
                        else
                            Toast.makeText(context, R.string.invalid_ip_port, Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}