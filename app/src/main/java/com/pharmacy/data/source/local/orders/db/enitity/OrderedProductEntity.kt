package com.pharmacy.data.source.local.orders.db.enitity

import androidx.room.ColumnInfo
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.OrderedProduct
import com.pharmacy.data.source.local.basket.db.enitity.ProductEntity
import com.pharmacy.data.source.local.orders.db.enitity.converter.OrderedProductEntityToOrderedProductConverter
import com.pharmacy.data.source.local.orders.db.enitity.converter.OrderedProductEntityTransformer

data class OrderedProductEntity(
    @ColumnInfo(name = "product")
    val product: ProductEntity,
    @ColumnInfo(name = "count")
    val count: Int,
) : ModelConverter<OrderedProductEntity, OrderedProduct> by OrderedProductEntityToOrderedProductConverter {
    companion object : ModelTransformer<OrderedProduct, OrderedProductEntity> by OrderedProductEntityTransformer
}