package com.pharmacy.ui.dialog.address_picker.model.mvi

import android.os.Parcelable
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.ui.model.AddressItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressPickerViewState(
    val query: String = emptyString(),
    val isQueryLoading: Boolean = false,
    val addresses: List<AddressItem> = emptyList(),
) : Parcelable