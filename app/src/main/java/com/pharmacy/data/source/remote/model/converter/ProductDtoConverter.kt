package com.pharmacy.data.source.remote.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.orZero
import com.pharmacy.data.model.Price
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.remote.model.PriceDto
import com.pharmacy.data.source.remote.model.ProductDto

object ProductDtoToProductConverter : ModelConverter<ProductDto, Product> {
    override fun convert(value: ProductDto): Product {
        return Product(
            id = value.id.orZero(),
            name = value.name.orEmpty(),
            imageUrl = value.imageUrl.orEmpty(),
            description = value.description.orEmpty(),
            category = value.category.orEmpty(),
            price = value.price?.convert() ?: Price.Empty,
        )
    }
}

object ProductToProductDtoConverter : ModelConverter<Product, ProductDto> {
    override fun convert(value: Product): ProductDto {
        return ProductDto(
            id = value.id,
            name = value.name,
            imageUrl = value.imageUrl,
            description = value.description,
            price = PriceDto.from(value.price),
        )
    }
}

object ProductDtoTransformer : ModelTransformer<Product, ProductDto> {
    override val source = ProductToProductDtoConverter
    override val receiver = ProductDtoToProductConverter
}