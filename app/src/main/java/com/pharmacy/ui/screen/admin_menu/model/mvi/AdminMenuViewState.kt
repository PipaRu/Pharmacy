package com.pharmacy.ui.screen.admin_menu.model.mvi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminMenuViewState(
    val data: Unit = Unit,
) : Parcelable