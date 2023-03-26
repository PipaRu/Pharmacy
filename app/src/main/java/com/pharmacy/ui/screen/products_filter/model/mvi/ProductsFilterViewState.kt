package com.pharmacy.ui.screen.products_filter.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.products_filter.model.PriceRange
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsFilterViewState(
    val query: String? = null,
    val isLoading: Boolean = true,
    val isSearchLoading: Boolean = false,
    val priceRange: PriceRange = PriceRange(
        start = 0,
        end = 10_000,
        currentStart = 0,
        currentEnd = 10_000,
    ),
    val categories: List<SelectableItem<CategoryItem>> = emptyList(),
    val productCount: Int = 0,
) : Parcelable