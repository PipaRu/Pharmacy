package com.pharmacy.ui.screen.product_details.model.mvi

sealed class ProductDetailsSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : ProductDetailsSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : ProductDetailsSideEffect()
    object NavigateBack : ProductDetailsSideEffect()
}