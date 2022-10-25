package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Address
import com.pharmacy.ui.model.AddressItem

object AddressItemToAddressConverter : ModelConverter<AddressItem, Address> {
    override fun convert(value: AddressItem): Address {
        return Address(
            id = value.id,
            name = value.name
        )
    }
}

object AddressToAddressItemConverter : ModelConverter<Address, AddressItem> {
    override fun convert(value: Address): AddressItem {
        return AddressItem(
            id = value.id,
            name = value.name
        )
    }
}

object AddressItemTransformer : ModelTransformer<Address, AddressItem> {
    override val source = AddressToAddressItemConverter
    override val receiver = AddressItemToAddressConverter
}