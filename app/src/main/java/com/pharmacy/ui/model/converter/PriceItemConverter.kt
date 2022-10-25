package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Price
import com.pharmacy.ui.model.PriceItem

object PriceItemToPriceConverter : ModelConverter<PriceItem, Price> {
    override fun convert(value: PriceItem): Price {
        return Price(
            finalPrice = value.finalPrice,
            startPrice = value.startPrice,
            discount = value.discount
        )
    }
}

object PriceToPriceItemConverter : ModelConverter<Price, PriceItem> {
    override fun convert(value: Price): PriceItem {
        return PriceItem(
            finalPrice = value.finalPrice,
            startPrice = value.startPrice,
            discount = value.discount
        )
    }
}

object PriceItemTransformer : ModelTransformer<Price, PriceItem> {
    override val source = PriceToPriceItemConverter
    override val receiver = PriceItemToPriceConverter
}