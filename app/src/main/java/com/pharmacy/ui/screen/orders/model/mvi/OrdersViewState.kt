package com.pharmacy.ui.screen.orders.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.OrderItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrdersViewState(
    val isContentLoading: Boolean = true,
    val isContentLoadingFailed: Boolean = false,
    val orders: List<OrderItem> = emptyList(),
) : Parcelable