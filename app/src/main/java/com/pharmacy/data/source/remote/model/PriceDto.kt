package com.pharmacy.data.source.remote.model

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Price
import com.pharmacy.data.source.remote.model.converter.PriceDtoToPriceConverter
import com.pharmacy.data.source.remote.model.converter.PriceDtoTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceDto(
    @SerialName("value")
    val value: Double? = null,
    @SerialName("discount")
    val discount: Double? = null,
) : ModelConverter<PriceDto, Price> by PriceDtoToPriceConverter {
    companion object : ModelTransformer<Price, PriceDto> by PriceDtoTransformer
}