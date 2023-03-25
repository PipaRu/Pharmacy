@file:OptIn(ExperimentalComposeUiApi::class)

package com.pharmacy.ui.dialog.authorization_required.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pharmacy.R
import com.pharmacy.common.ui.compose.text.field.PhoneNumberTextField
import com.pharmacy.ui.dialog.authorization_required.AuthorizationRequiredDialogViewModel
import com.pharmacy.ui.dialog.authorization_required.mvi.AuthorizationRequiredViewState
import com.pharmacy.ui.kit.ActionButton
import com.pharmacy.ui.kit.ActionButtonState
import com.pharmacy.ui.model.PhoneNumberItem
import com.pharmacy.ui.model.UserStatusItem
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_authorization_required_message as message

@Composable
fun AuthorizationRequiredScreenContent(viewModel: AuthorizationRequiredDialogViewModel) {
    val state: AuthorizationRequiredViewState by viewModel.collectAsState()
    when (val status = state.status) {
        is UserStatusItem.Unauthorized -> {
            AuthorizationRequiredContent(
                name = status.name,
                phone = status.phone,
                isLoginAvailable = status.isLoginAvailable,
                isLoginProcess = state.isLoading,
                onChange = { name, phone -> viewModel.onUnauthorizedDataChange(name, phone) },
                onLogin = { name, phone -> viewModel.login(name = name, phone = phone) }
            )
        }
        is UserStatusItem.Admin -> {
            AuthorizationRequiredContent(
                name = status.profile.name,
                phone = status.profile.phone,
                isLoginAvailable = false,
                isLoginProcess = state.isLoading,
                onChange = { _, _ -> },
                onLogin = { _, _ -> }
            )
        }
        is UserStatusItem.User -> {
            AuthorizationRequiredContent(
                name = status.profile.name,
                phone = status.profile.phone,
                isLoginAvailable = false,
                isLoginProcess = state.isLoading,
                onChange = { _, _ -> },
                onLogin = { _, _ -> }
            )
        }
    }

}

@Composable
fun AuthorizationRequiredContent(
    name: String,
    phone: PhoneNumberItem,
    isLoginProcess: Boolean,
    isLoginAvailable: Boolean,
    onChange: (name: String, phone: PhoneNumberItem) -> Unit,
    onLogin: (name: String, phone: PhoneNumberItem) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(message),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 32.dp, vertical = 16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { newName -> onChange.invoke(newName, phone) },
                maxLines = 1,
                enabled = !isLoginProcess,
                label = { Text(text = stringResource(R.string.content_profile_unauthorized_name_placeholder)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            PhoneNumberTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.content_profile_unauthorized_phone_placeholder),
                number = phone.number,
                enabled = !isLoginProcess,
                onPhoneChanged = { newPhone ->
                    onChange.invoke(
                        name,
                        phone.copy(number = newPhone)
                    )
                },
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Spacer(modifier = Modifier.height(16.dp))

            ActionButton(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = stringResource(R.string.content_profile_unauthorized_action_login),
                icon = Icons.Default.Login,
                onClick = { onLogin.invoke(name, phone) },
                state = when {
                    isLoginProcess -> ActionButtonState.LOADING
                    !isLoginAvailable -> ActionButtonState.DISABLED
                    else -> ActionButtonState.ENABLED
                }
            )
        }
    }
}