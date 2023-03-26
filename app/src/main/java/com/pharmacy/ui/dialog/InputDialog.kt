package com.pharmacy.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun InputDialog(
    text: String,
    hint: String? = null,
    negativeText: String? = null,
    actionText: String = stringResource(id = android.R.string.ok),
    onNext: (String) -> Unit,
    onCancel: () -> Unit,
    onNegative: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(),
    ) {
        var value: String by remember { mutableStateOf(text) }
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = { value = it },
                    label = if (hint != null) {
                        {
                            Text(text = hint)
                        }
                    } else {
                        null
                    },
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.align(Alignment.End).wrapContentSize()
                ) {
                    if (negativeText != null) {
                        Button(
                            modifier = Modifier,
                            onClick = { onNegative?.invoke() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.error,
                            )
                        ) {
                            Text(text = negativeText)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier,
                        onClick = { onNext.invoke(value) },
                    ) {
                        Text(text = actionText)
                    }
                }
            }
        }
    }
}