package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.Product
import com.pharmacy.ui.model.PriceItem
import com.pharmacy.ui.model.ProductItem

object ProductItemToProductConverter : ModelConverter<ProductItem, Product> {
    override fun convert(value: ProductItem): Product {
        return Product(
            id = value.id,
            name = value.name,
            imageUrl = value.imageUrl,
            description = value.description,
            category = value.category,
            price = value.price.convert(),
        )
    }
}

object ProductToProductItemConverter : ModelConverter<Product, ProductItem> {
    override fun convert(value: Product): ProductItem {
        return ProductItem(
            id = value.id,
            name = value.name,
            imageUrl = value.imageUrl,
            description = value.description,
            category = value.category,
            price = PriceItem.from(value.price),
        )
    }
}

object ProductItemTransformer : ModelTransformer<Product, ProductItem> {
    override val source = ProductToProductItemConverter
    override val receiver = ProductItemToProductConverter
}