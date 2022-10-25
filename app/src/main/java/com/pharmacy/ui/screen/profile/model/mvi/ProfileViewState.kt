package com.pharmacy.ui.screen.profile.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.UserStatusItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileViewState(
    val status: UserStatusItem = UserStatusItem.Unauthorized.Default,
    val isLoading: Boolean = false,
) : Parcelable