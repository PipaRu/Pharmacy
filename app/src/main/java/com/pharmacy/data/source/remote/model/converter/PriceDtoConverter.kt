package com.pharmacy.data.source.remote.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.orZero
import com.pharmacy.data.model.Price
import com.pharmacy.data.source.remote.model.PriceDto

object PriceDtoToPriceConverter : ModelConverter<PriceDto, Price> {
    override fun convert(value: PriceDto): Price {
        val priceValue = value.value.orZero()
        val discountValue = value.discount.orZero()
        return Price(
            finalPrice = priceValue - (priceValue * (discountValue / 100.0)),
            startPrice = priceValue,
            discount = discountValue
        )
    }
}

object PriceToPriceDtoConverter : ModelConverter<Price, PriceDto> {
    override fun convert(value: Price): PriceDto {
        return PriceDto(
            value = value.startPrice,
            discount = value.discount
        )
    }
}

object PriceDtoTransformer : ModelTransformer<Price, PriceDto> {
    override val source = PriceToPriceDtoConverter
    override val receiver = PriceDtoToPriceConverter
}