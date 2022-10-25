package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.Profile
import com.pharmacy.ui.model.AddressItem
import com.pharmacy.ui.model.PhoneNumberItem
import com.pharmacy.ui.model.ProfileItem

object ProfileItemToProfileConverter : ModelConverter<ProfileItem, Profile> {
    override fun convert(value: ProfileItem): Profile {
        return Profile(
            id = value.id,
            name = value.name,
            email = value.email,
            phone = value.phone.convert(),
            address = value.address?.convert()
        )
    }
}

object ProfileToProfileItemConverter : ModelConverter<Profile, ProfileItem> {
    override fun convert(value: Profile): ProfileItem {
        return ProfileItem(
            id = value.id,
            name = value.name,
            email = value.email,
            phone = PhoneNumberItem.from(value.phone),
            address = value.address?.let(AddressItem.Companion::from)
        )
    }
}

object ProfileItemTransformer : ModelTransformer<Profile, ProfileItem> {
    override val source = ProfileToProfileItemConverter
    override val receiver = ProfileItemToProfileConverter
}