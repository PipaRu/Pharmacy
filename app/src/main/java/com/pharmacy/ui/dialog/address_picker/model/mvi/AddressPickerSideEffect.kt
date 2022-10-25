package com.pharmacy.ui.dialog.address_picker.model.mvi

import com.pharmacy.ui.model.AddressItem

sealed class AddressPickerSideEffect {
    data class AddressSelected(val address: AddressItem) : AddressPickerSideEffect()
}