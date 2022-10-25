package com.pharmacy.data.source.local.basket.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Price
import com.pharmacy.data.source.local.basket.db.enitity.PriceEntity

object PriceEntityToPriceConverter : ModelConverter<PriceEntity, Price> {
    override fun convert(value: PriceEntity): Price {
        return Price(
            finalPrice = value.finalPrice,
            startPrice = value.startPrice,
            discount = value.discount
        )
    }
}

object PriceToPriceEntityConverter : ModelConverter<Price, PriceEntity> {
    override fun convert(value: Price): PriceEntity {
        return PriceEntity(
            finalPrice = value.finalPrice,
            startPrice = value.startPrice,
            discount = value.discount
        )
    }
}

object PriceEntityTransformer : ModelTransformer<Price, PriceEntity> {
    override val source = PriceToPriceEntityConverter
    override val receiver = PriceEntityToPriceConverter
}