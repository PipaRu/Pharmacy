package com.pharmacy.ui.screen.admin_product_details.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.ProductItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminProductDetailsViewState(
    val isContentLoading: Boolean = true,
    val isContentEditingLoading: Boolean = false,
    val isContentDeletionLoading: Boolean = false,
    val product: ProductItem = ProductItem.Empty,
) : Parcelable