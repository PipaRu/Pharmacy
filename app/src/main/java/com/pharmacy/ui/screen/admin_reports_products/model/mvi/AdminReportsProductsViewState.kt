package com.pharmacy.ui.screen.admin_reports_products.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminReportsProductsViewState(
    val isInitialLoading: Boolean = false,
    val query: String? = null,
    val products: Resource<ParcelableList<ProductItem>> = Resource.Nothing,
    val selectedProductStatistic: Resource<ProductStatistic> = Resource.Nothing,
) : Parcelable