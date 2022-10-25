package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.UserStatus
import com.pharmacy.ui.model.ProfileItem
import com.pharmacy.ui.model.UserStatusItem

object UserStatusItemToUserStatusConverter : ModelConverter<UserStatusItem, UserStatus> {
    override fun convert(value: UserStatusItem): UserStatus {
        return when (value) {
            is UserStatusItem.Admin -> UserStatus.Admin(value.profile.convert())
            is UserStatusItem.Unauthorized -> UserStatus.Unauthorized
            is UserStatusItem.User -> UserStatus.User(value.profile.convert())
        }
    }
}

object UserStatusToUserStatusItemConverter : ModelConverter<UserStatus, UserStatusItem> {
    override fun convert(value: UserStatus): UserStatusItem {
        return when (value) {
            is UserStatus.Admin -> {
                UserStatusItem.Admin(profile = ProfileItem.from(value.profile))
            }
            is UserStatus.Unauthorized -> {
                UserStatusItem.Unauthorized.Default
            }
            is UserStatus.User -> {
                UserStatusItem.User(profile = ProfileItem.from(value.profile))
            }
        }
    }
}

object UserStatusItemTransformer : ModelTransformer<UserStatus, UserStatusItem> {
    override val source = UserStatusToUserStatusItemConverter
    override val receiver = UserStatusItemToUserStatusConverter
}