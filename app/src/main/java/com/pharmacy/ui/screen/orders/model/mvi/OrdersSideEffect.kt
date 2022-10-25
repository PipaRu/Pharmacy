package com.pharmacy.ui.screen.orders.model.mvi

import com.pharmacy.ui.model.OrderItem

sealed class OrdersSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : OrdersSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : OrdersSideEffect()
    object NavigateBack : OrdersSideEffect()
    data class CancelOrderWarning(val order: OrderItem) : OrdersSideEffect()
}