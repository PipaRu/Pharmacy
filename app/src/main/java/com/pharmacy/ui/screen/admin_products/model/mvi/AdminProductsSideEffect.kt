package com.pharmacy.ui.screen.admin_products.model.mvi

import com.pharmacy.ui.model.ProductItem

sealed class AdminProductsSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : AdminProductsSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : AdminProductsSideEffect()
    object NavigateBack : AdminProductsSideEffect()
    object ShowDeleteAllSelectedProductsWarning : AdminProductsSideEffect()
    data class NavigateToProductDetails(val productItem: ProductItem) : AdminProductsSideEffect()
}