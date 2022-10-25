package com.pharmacy.data.source.local.basket.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.local.basket.db.enitity.PriceEntity
import com.pharmacy.data.source.local.basket.db.enitity.ProductEntity

object ProductEntityToProductConverter : ModelConverter<ProductEntity, Product> {
    override fun convert(value: ProductEntity): Product {
        return Product(
            id = value.id,
            name = value.name,
            imageUrl = value.imageUrl,
            description = value.description,
            category = value.category,
            price = value.price.convert()
        )
    }
}

object ProductToProductEntityConverter : ModelConverter<Product, ProductEntity> {
    override fun convert(value: Product): ProductEntity {
        return ProductEntity(
            id = value.id,
            name = value.name,
            imageUrl = value.imageUrl,
            description = value.description,
            category = value.category,
            price = PriceEntity.from(value.price)
        )
    }
}

object ProductEntityTransformer : ModelTransformer<Product, ProductEntity> {
    override val source = ProductToProductEntityConverter
    override val receiver = ProductEntityToProductConverter
}