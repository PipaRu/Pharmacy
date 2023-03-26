package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.*
import com.pharmacy.data.model.Product
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.PriceItem
import com.pharmacy.ui.model.ProductItem

object ProductItemToProductConverter : ModelConverter<ProductItem, Product> {
    override fun convert(value: ProductItem): Product = Product(
        id = value.id,
        name = value.name,
        imageUrl = value.imageUrl,
        description = value.description,
        categories = value.categories.convertAll(),
        price = value.price.convert(),
    )
}

object ProductToProductItemConverter : ModelConverter<Product, ProductItem> {
    override fun convert(value: Product): ProductItem = ProductItem(
        id = value.id,
        name = value.name,
        imageUrl = value.imageUrl,
        description = value.description,
        categories = CategoryItem.fromAll(value.categories),
        price = PriceItem.from(value.price),
    )
}

object ProductItemTransformer : ModelTransformer<Product, ProductItem> {
    override val source = ProductToProductItemConverter
    override val receiver = ProductItemToProductConverter
}