package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.ui.model.converter.BasketBunchItemToBasketBunchConverter
import com.pharmacy.ui.model.converter.BasketBunchItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasketBunchItem(
    val id: Int,
    val product: ProductItem,
    val count: Int,
) : Parcelable,
    ModelConverter<BasketBunchItem, BasketBunch> by BasketBunchItemToBasketBunchConverter {
    companion object : ModelTransformer<BasketBunch, BasketBunchItem> by BasketBunchItemTransformer
}
