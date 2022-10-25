package com.pharmacy.data.source.local.basket.db.enitity

import androidx.room.ColumnInfo
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Price
import com.pharmacy.data.source.local.basket.db.enitity.converter.PriceEntityToPriceConverter
import com.pharmacy.data.source.local.basket.db.enitity.converter.PriceEntityTransformer

data class PriceEntity(
    @ColumnInfo(name = "price_final")
    val finalPrice: Double,
    @ColumnInfo(name = "price_start")
    val startPrice: Double,
    @ColumnInfo(name = "discount")
    val discount: Double,
) : ModelConverter<PriceEntity, Price> by PriceEntityToPriceConverter {
    companion object : ModelTransformer<Price, PriceEntity> by PriceEntityTransformer
}