package com.pharmacy.ui.dialog.authorization_required.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.UserStatusItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorizationRequiredViewState(
    val status: UserStatusItem = UserStatusItem.Unauthorized.Default,
    val isLoading: Boolean = false,
) : Parcelable