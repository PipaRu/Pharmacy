package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.Zero
import com.pharmacy.data.model.Price
import com.pharmacy.ui.model.converter.PriceItemToPriceConverter
import com.pharmacy.ui.model.converter.PriceItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceItem(
    val finalPrice: Double,
    val startPrice: Double,
    val discount: Double,
) : Parcelable, ModelConverter<PriceItem, Price> by PriceItemToPriceConverter {
    companion object : ModelTransformer<Price, PriceItem> by PriceItemTransformer {
        val Empty: PriceItem = PriceItem(
            finalPrice = Double.Zero,
            startPrice = Double.Zero,
            discount = Double.Zero,
        )
    }
}