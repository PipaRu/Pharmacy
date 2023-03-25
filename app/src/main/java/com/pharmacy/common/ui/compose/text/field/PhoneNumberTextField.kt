@file:OptIn(ExperimentalComposeUiApi::class)

package com.pharmacy.common.ui.compose.text.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.pharmacy.common.formatter.PhoneFormatter

@Composable
fun PhoneNumberTextField(
    modifier: Modifier = Modifier,
    label: String,
    number: String,
    enabled: Boolean = true,
    onPhoneChanged: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        modifier = modifier,
        value = number,
        maxLines = 1,
        enabled = enabled,
        onValueChange = { newNumber ->
            val isLengthCorrect = newNumber.length <= PhoneFormatter.MAX_NUMBERS
            val isDigitsOnly = newNumber.isDigitsOnly()
            if (isLengthCorrect && isDigitsOnly) {
                onPhoneChanged.invoke(newNumber)
            }
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.caption
            )
        },
        visualTransformation = VisualTransformations.phoneNumber,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = imeAction),
        keyboardActions = keyboardActions,
    )
}