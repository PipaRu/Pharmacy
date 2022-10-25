@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.dialog.address_picker.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pharmacy.ui.dialog.address_picker.AddressPickerViewModel
import com.pharmacy.ui.dialog.address_picker.model.mvi.AddressPickerViewState
import com.pharmacy.ui.model.AddressItem
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_address_picker_input_label as input_label
import com.pharmacy.R.string.content_address_picker_message_items_empty as message_items_empty

@Composable
fun AddressPickerScreenContent(
    viewModel: AddressPickerViewModel,
) {
    val state: AddressPickerViewState by viewModel.collectAsState()
    AddressPickerContent(
        state = state,
        onQueryChanged = viewModel::search,
        onAddressSelected = viewModel::select
    )
}

@Composable
private fun AddressPickerContent(
    state: AddressPickerViewState,
    onQueryChanged: (String) -> Unit,
    onAddressSelected: (AddressItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AddressInputComponent(
            query = state.query,
            onQueryChanged = onQueryChanged
        )
        Spacer(modifier = Modifier.height(8.dp))
        when {
            state.isQueryLoading -> AddressPickerQueryLoadingContent()
            state.addresses.isEmpty() -> AddressPickerEmptyContent()
            else -> AddressPickerAddressListContent(
                addresses = state.addresses,
                onAddressSelected = onAddressSelected
            )
        }
    }
}

@Composable
private fun AddressInputComponent(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChanged,
        label = {
            Text(text = stringResource(input_label))
        }
    )
}

@Composable
private fun AddressPickerEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(message_items_empty),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AddressPickerQueryLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AddressPickerAddressListContent(
    addresses: List<AddressItem>,
    onAddressSelected: (AddressItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(addresses) { address ->
            AddressItemComponent(
                address = address,
                onClick = { onAddressSelected.invoke(address) }
            )
        }
    }
}

@Composable
private fun AddressItemComponent(
    address: AddressItem,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 12.dp)
        ) {
            Text(
                text = address.name,
                style = MaterialTheme.typography.body2
            )
        }
    }
}