package com.pharmacy.data.source.local.basket.db.enitity.converter

import com.pharmacy.common.converter.model.*
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.local.basket.db.enitity.CategoryEntity
import com.pharmacy.data.source.local.basket.db.enitity.PriceEntity
import com.pharmacy.data.source.local.basket.db.enitity.ProductEntity

object ProductEntityToProductConverter : ModelConverter<ProductEntity, Product> {
    override fun convert(value: ProductEntity): Product = Product(
        id = value.id,
        name = value.name,
        imageUrl = value.imageUrl,
        description = value.description,
        categories = value.categories.convertAll(),
        price = value.price.convert()
    )
}

object ProductToProductEntityConverter : ModelConverter<Product, ProductEntity> {
    override fun convert(value: Product): ProductEntity = ProductEntity(
        id = value.id,
        name = value.name,
        imageUrl = value.imageUrl,
        description = value.description,
        categories = CategoryEntity.fromAll(value.categories),
        price = PriceEntity.from(value.price)
    )
}

object ProductEntityTransformer : ModelTransformer<Product, ProductEntity> {
    override val source = ProductToProductEntityConverter
    override val receiver = ProductEntityToProductConverter
}