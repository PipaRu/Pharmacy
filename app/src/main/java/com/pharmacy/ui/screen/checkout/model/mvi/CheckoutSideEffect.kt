package com.pharmacy.ui.screen.checkout.model.mvi

sealed class CheckoutSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : CheckoutSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : CheckoutSideEffect()
    object AddressSelection : CheckoutSideEffect()
    object NavigateBack : CheckoutSideEffect()
    object NavigateToLogin : CheckoutSideEffect()
    object NavigateNextAfterCheckout : CheckoutSideEffect()
}