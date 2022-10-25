package com.pharmacy.data.source.remote.model

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.remote.model.converter.ProductDtoToProductConverter
import com.pharmacy.data.source.remote.model.converter.ProductDtoTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("price")
    val price: PriceDto? = null,
) : ModelConverter<ProductDto, Product> by ProductDtoToProductConverter {
    companion object : ModelTransformer<Product, ProductDto> by ProductDtoTransformer
}