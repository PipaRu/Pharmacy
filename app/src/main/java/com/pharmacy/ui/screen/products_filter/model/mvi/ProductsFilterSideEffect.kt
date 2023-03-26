package com.pharmacy.ui.screen.products_filter.model.mvi

import com.pharmacy.data.model.ProductFilter

sealed class ProductsFilterSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : ProductsFilterSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : ProductsFilterSideEffect()
    object NavigateBack : ProductsFilterSideEffect()
    data class ApplyFilter(val filter: ProductFilter?) : ProductsFilterSideEffect()
}