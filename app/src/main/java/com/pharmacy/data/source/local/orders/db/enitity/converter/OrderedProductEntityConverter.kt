package com.pharmacy.data.source.local.orders.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.OrderedProduct
import com.pharmacy.data.source.local.basket.db.enitity.ProductEntity
import com.pharmacy.data.source.local.orders.db.enitity.OrderedProductEntity

object OrderedProductEntityToOrderedProductConverter :
    ModelConverter<OrderedProductEntity, OrderedProduct> {
    override fun convert(value: OrderedProductEntity): OrderedProduct {
        return OrderedProduct(
            product = value.product.convert(),
            count = value.count
        )
    }
}

object OrderedProductToOrderedProductEntityConverter :
    ModelConverter<OrderedProduct, OrderedProductEntity> {
    override fun convert(value: OrderedProduct): OrderedProductEntity {
        return OrderedProductEntity(
            product = ProductEntity.from(value.product),
            count = value.count
        )
    }
}

object OrderedProductEntityTransformer : ModelTransformer<OrderedProduct, OrderedProductEntity> {
    override val source = OrderedProductToOrderedProductEntityConverter
    override val receiver = OrderedProductEntityToOrderedProductConverter
}