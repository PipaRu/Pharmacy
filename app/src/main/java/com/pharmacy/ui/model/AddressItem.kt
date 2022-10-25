package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Address
import com.pharmacy.ui.model.converter.AddressItemToAddressConverter
import com.pharmacy.ui.model.converter.AddressItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressItem(
    val id: Long,
    val name: String,
) : Parcelable, ModelConverter<AddressItem, Address> by AddressItemToAddressConverter {
    companion object : ModelTransformer<Address, AddressItem> by AddressItemTransformer
}