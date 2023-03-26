package com.pharmacy.ui.screen.product_details.model.mvi

import android.os.Parcelable
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.ProductItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetailsViewState(
    val isContentLoading: Boolean = true,
    val isInBasketLoading: Boolean = false,
    val product: ProductItem = ProductItem.Empty,
    val basketBunch: BasketBunchItem? = null,
) : Parcelable