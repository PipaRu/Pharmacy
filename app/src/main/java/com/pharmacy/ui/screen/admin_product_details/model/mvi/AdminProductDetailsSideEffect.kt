package com.pharmacy.ui.screen.admin_product_details.model.mvi

sealed class AdminProductDetailsSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : AdminProductDetailsSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : AdminProductDetailsSideEffect()
    object NavigateBack : AdminProductDetailsSideEffect()
    object ShowDeletionProductWarning : AdminProductDetailsSideEffect()
}