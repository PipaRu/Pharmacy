package com.pharmacy.ui.screen.products_filter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRange(
    val start: Int,
    val end: Int,
    val currentStart: Int,
    val currentEnd: Int,
) : Parcelable {

    companion object {
        val Empty: PriceRange = PriceRange(
            start = 0,
            end = 0,
            currentStart = 0,
            currentEnd = 0,
        )
    }

}