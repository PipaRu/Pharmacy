@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

package com.pharmacy.ui.screen.profile.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pharmacy.R
import com.pharmacy.common.formatter.PhoneFormatter
import com.pharmacy.common.ui.compose.layout.plus
import com.pharmacy.common.ui.compose.text.field.PhoneNumberTextField
import com.pharmacy.ui.model.AddressItem
import com.pharmacy.ui.model.PhoneNumberItem
import com.pharmacy.ui.model.ProfileItem
import com.pharmacy.ui.model.UserStatusItem
import com.pharmacy.ui.screen.profile.ProfileViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProfileContent(viewModel: ProfileViewModel) {
    val state by viewModel.collectAsState()
    if (state.isLoading) {
        LocalSoftwareKeyboardController.current?.hide()
    }
    when (val status = state.status) {
        is UserStatusItem.Unauthorized -> UnauthorizedContent(
            name = status.name,
            phone = status.phone,
            isLoginAvailable = status.isLoginAvailable,
            isLoginProcess = state.isLoading,
            onChange = { name, phone -> viewModel.onUnauthorizedDataChange(name, phone) },
            onLogin = { name, phone -> viewModel.login(name = name, phone = phone) }
        )
        is UserStatusItem.Admin -> {
            AuthorizedContent(
                profile = status.profile,
                isAdmin = true,
                onProfileSettings = { viewModel.onProfileSettings() },
                onLogout = { viewModel.logout() },
                onFavorites = { viewModel.onFavorites() },
                onOrders = { viewModel.onOrders() },
                onSettings = { viewModel.onSettings() },
                onAdministration = { viewModel.onAdministration() },
                onSelectAddressAction = viewModel::selectAddress,
                onDeleteAddressAction = viewModel::deleteAddress
            )
        }
        is UserStatusItem.User -> {
            AuthorizedContent(
                profile = status.profile,
                isAdmin = false,
                onProfileSettings = { viewModel.onProfileSettings() },
                onLogout = { viewModel.logout() },
                onFavorites = { viewModel.onFavorites() },
                onOrders = { viewModel.onOrders() },
                onSettings = { viewModel.onSettings() },
                onAdministration = { viewModel.onAdministration() },
                onSelectAddressAction = viewModel::selectAddress,
                onDeleteAddressAction = viewModel::deleteAddress
            )
        }
    }
}

@Composable
private fun UnauthorizedContent(
    name: String,
    phone: PhoneNumberItem,
    isLoginProcess: Boolean,
    isLoginAvailable: Boolean,
    onChange: (name: String, phone: PhoneNumberItem) -> Unit,
    onLogin: (name: String, phone: PhoneNumberItem) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.content_profile_title)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues + PaddingValues(horizontal = 32.dp, vertical = 16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { newName -> onChange.invoke(newName, phone) },
                maxLines = 1,
                enabled = !isLoginProcess,
                label = { Text(text = stringResource(R.string.content_profile_unauthorized_name_placeholder)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            PhoneNumberTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.content_profile_unauthorized_phone_placeholder),
                number = phone.number,
                enabled = !isLoginProcess,
                onPhoneChanged = { newPhone ->
                    onChange.invoke(name,
                        phone.copy(number = newPhone))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(0.7f),
                onClick = {
                    onLogin.invoke(name, phone)
                },
                enabled = !isLoginProcess && isLoginAvailable
            ) {
                if (isLoginProcess) {
                    CircularProgressIndicator()
                } else {
                    Row {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.Login,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = stringResource(R.string.content_profile_unauthorized_action_login),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthorizedContent(
    profile: ProfileItem,
    isAdmin: Boolean,
    onProfileSettings: () -> Unit,
    onLogout: () -> Unit,
    onFavorites: () -> Unit,
    onOrders: () -> Unit,
    onSettings: () -> Unit,
    onAdministration: () -> Unit,
    onSelectAddressAction: () -> Unit,
    onDeleteAddressAction: () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        onClick = onProfileSettings,
                        backgroundColor = MaterialTheme.colors.primarySurface
                    ) {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .size(44.dp)
                                    .align(Alignment.CenterVertically),
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = null
                            )
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = profile.name,
                                    style = MaterialTheme.typography.h6
                                )
                                Text(
                                    text = PhoneFormatter.format(profile.phone),
                                    style = MaterialTheme.typography.subtitle2
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = { paddings ->
            LazyColumn(
                contentPadding = paddings + PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    AddressComponent(
                        address = profile.address,
                        onSelectAddressAction = onSelectAddressAction,
                        onDeleteAddressAction = onDeleteAddressAction
                    )
                }
                item {
                    SectionComponent(
                        icon = Icons.Default.Favorite,
                        text = stringResource(R.string.content_profile_authorized_item_favorites),
                        onClick = onFavorites
                    )
                }
                item {
                    SectionComponent(
                        icon = Icons.Default.Archive,
                        text = stringResource(R.string.content_profile_authorized_item_orders),
                        onClick = onOrders
                    )
                }
                item {
                    SectionComponent(
                        icon = Icons.Default.Settings,
                        text = stringResource(R.string.content_profile_authorized_item_settings),
                        onClick = onSettings
                    )
                }
                if (isAdmin) {
                    item {
                        Divider(thickness = 2.dp)
                    }
                    item {
                        SectionComponent(
                            icon = Icons.Default.AdminPanelSettings,
                            text = stringResource(R.string.content_profile_authorized_item_admin),
                            onClick = onAdministration
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun AddressComponent(
    address: AddressItem?,
    onSelectAddressAction: () -> Unit,
    onDeleteAddressAction: () -> Unit,
) {
    Card(
        modifier = Modifier,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (address == null) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.content_profile_address_message_not_selected),
                    style = MaterialTheme.typography.subtitle1
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onSelectAddressAction
                ) {
                    Text(text = stringResource(R.string.content_profile_address_action_select))
                }
            } else {
                Text(
                    text = stringResource(R.string.content_profile_address_label),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = address.name,
                    style = MaterialTheme.typography.caption
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                ) {
                    Button(onClick = onSelectAddressAction) {
                        Text(text = stringResource(R.string.content_profile_address_action_change))
                    }
                    Button(onClick = onDeleteAddressAction) {
                        Text(text = stringResource(R.string.content_profile_address_action_delete))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionComponent(
    icon: ImageVector,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterVertically),
                imageVector = icon,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = text,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Start
            )
        }
    }
}
