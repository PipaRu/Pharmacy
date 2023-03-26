package com.pharmacy.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductStatistic(
    val product: ProductItem,
    val totalOrdersCount: Int,
    val ordersInWeek: Int,
    val totalProfit: Double,
) : Parcelable