package com.pharmacy.ui.screen.admin_products.model.mvi

import android.os.Parcelable
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.model.SelectableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminProductsViewState(
    val items: List<SelectableItem<ProductItem>> = emptyList(),
    val query: String = emptyString(),
    val isSelectionMode: Boolean = false,
    val isContentLoading: Boolean = true,
    val isContentEditingLoading: Boolean = false,
) : Parcelable