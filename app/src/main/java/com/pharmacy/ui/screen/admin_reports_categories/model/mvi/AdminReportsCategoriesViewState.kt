package com.pharmacy.ui.screen.admin_reports_categories.model.mvi

import android.os.Parcelable
import com.pharmacy.ui.model.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminReportsCategoriesViewState(
    val isInitialLoading: Boolean = true,
    val query: String? = null,
    val categories: Resource<ParcelableList<CategoryItem>> = Resource.Nothing,
    val selectedCategoryStatistic: Resource<CategoryStatistic> = Resource.Nothing,
) : Parcelable