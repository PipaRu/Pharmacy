package com.pharmacy.ui.screen.basket.model.mvi

import android.os.Parcelable
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.SelectableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasketViewState(
    val items: List<SelectableItem<BasketBunchItem>> = emptyList(),
    val query: String = emptyString(),
    val isProductsLoading: Boolean = true,
    val isPurchaseProcessLoading: Boolean = true,
) : Parcelable