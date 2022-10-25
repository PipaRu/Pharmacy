package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.data.model.UserStatus
import com.pharmacy.ui.model.converter.UserStatusItemToUserStatusConverter
import com.pharmacy.ui.model.converter.UserStatusItemTransformer
import kotlinx.parcelize.Parcelize

sealed class UserStatusItem : Parcelable,
    ModelConverter<UserStatusItem, UserStatus> by UserStatusItemToUserStatusConverter {

    @Parcelize
    data class Unauthorized(
        val name: String,
        val phone: PhoneNumberItem,
        val isLoginAvailable: Boolean,
    ) : UserStatusItem() {
        companion object {
            val Empty = Unauthorized(
                emptyString(),
                phone = PhoneNumberItem(),
                isLoginAvailable = false
            )

            val Default = Unauthorized(
                name = emptyString(),
                phone = PhoneNumberItem(
                    number = emptyString()
                ),
                isLoginAvailable = false
            )
        }
    }

    @Parcelize
    data class User(val profile: ProfileItem) : UserStatusItem()

    @Parcelize
    data class Admin(val profile: ProfileItem) : UserStatusItem()

    companion object : ModelTransformer<UserStatus, UserStatusItem> by UserStatusItemTransformer

}