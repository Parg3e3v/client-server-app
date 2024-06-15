package com.parg3v.client_serverapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.parg3v.client_serverapp.R

@Composable
fun CustomServerDialog(
    onDismiss: () -> Unit,
    portProvider: () -> String,
    onPortChange: (String) -> Unit,
    ipProvider: () -> String
) {
    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(dimensionResource(id = R.dimen.padding_dialog_col))) {
                    Text(text = stringResource(R.string.title))
                    Spacer(Modifier.size(dimensionResource(id = R.dimen.dialog_spacer_1)))

                    OutlinedTextField(value = portProvider(), onValueChange = onPortChange)
                    Text(
                        text = stringResource(id = R.string.ip_text, ipProvider()),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}