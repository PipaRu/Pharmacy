package com.pharmacy.ui.screen.basket.model.mvi

import com.pharmacy.ui.model.BasketBunchItem

sealed class BasketSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : BasketSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : BasketSideEffect()
    object ShowAuthorizationRequired : BasketSideEffect()
    data class ShowCheckout(val products: List<BasketBunchItem>) : BasketSideEffect()
}