package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.Empty
import com.pharmacy.common.extensions.Zero
import com.pharmacy.data.model.Profile
import com.pharmacy.ui.model.converter.ProfileItemToProfileConverter
import com.pharmacy.ui.model.converter.ProfileItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileItem(
    val id: Int,
    val name: String,
    val email: String,
    val phone: PhoneNumberItem,
    val address: AddressItem?,
) : Parcelable, ModelConverter<ProfileItem, Profile> by ProfileItemToProfileConverter {

    companion object : ModelTransformer<Profile, ProfileItem> by ProfileItemTransformer {
        val Empty: ProfileItem = ProfileItem(
            id = Int.Zero,
            name = String.Empty,
            address = AddressItem(
                id = Long.Zero,
                name = String.Empty
            ),
            email = String.Empty,
            phone = PhoneNumberItem()
        )
    }

}