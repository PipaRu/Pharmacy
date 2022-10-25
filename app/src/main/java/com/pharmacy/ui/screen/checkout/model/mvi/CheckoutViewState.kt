package com.pharmacy.ui.screen.checkout.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.OrderItem
import com.pharmacy.ui.model.ProfileItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutViewState(
    val profile: ProfileItem? = null,
    val products: List<BasketBunchItem> = emptyList(),
    val isContentLoading: Boolean = true,
    val isCheckoutAvailable: Boolean = true,
    val isCheckoutProcessLoading: Boolean = false,
    val order: OrderItem? = null,
) : Parcelable