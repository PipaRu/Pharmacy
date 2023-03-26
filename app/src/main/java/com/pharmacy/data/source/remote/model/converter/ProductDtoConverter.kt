package com.pharmacy.data.source.remote.model.converter

import com.pharmacy.common.converter.model.*
import com.pharmacy.common.extensions.orZero
import com.pharmacy.data.model.Price
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.remote.model.CategoryDto
import com.pharmacy.data.source.remote.model.PriceDto
import com.pharmacy.data.source.remote.model.ProductDto

object ProductDtoToProductConverter : ModelConverter<ProductDto, Product> {
    override fun convert(value: ProductDto): Product = Product(
        id = value.id.orZero(),
        name = value.name.orEmpty(),
        imageUrl = value.imageUrl.orEmpty(),
        description = value.description.orEmpty(),
        categories = value.categories?.convertAll().orEmpty(),
        price = value.price?.convert() ?: Price.Empty,
    )
}

object ProductToProductDtoConverter : ModelConverter<Product, ProductDto> {
    override fun convert(value: Product): ProductDto = ProductDto(
        id = value.id,
        name = value.name,
        imageUrl = value.imageUrl,
        description = value.description,
        price = PriceDto.from(value.price),
        categories = CategoryDto.fromAll(value.categories),
    )
}

object ProductDtoTransformer : ModelTransformer<Product, ProductDto> {
    override val source = ProductToProductDtoConverter
    override val receiver = ProductDtoToProductConverter
}