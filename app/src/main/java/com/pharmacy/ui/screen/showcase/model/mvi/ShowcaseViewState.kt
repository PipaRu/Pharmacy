package com.pharmacy.ui.screen.showcase.model.mvi

import android.os.Parcelable
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.ui.screen.showcase.model.ShowcaseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowcaseViewState(
    val items: List<ShowcaseItem> = emptyList(),
    val query: String = emptyString(),
    val isProductsLoading: Boolean = true,
    val productsLoadingError: Throwable? = null,
) : Parcelable
