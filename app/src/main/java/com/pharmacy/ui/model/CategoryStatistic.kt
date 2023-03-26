package com.pharmacy.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryStatistic(
    val category: CategoryItem,
    val productsCount: Int,
    val totalOrdersCount: Int,
    val ordersInWeek: Int,
    val totalProfit: Double,
) : Parcelable