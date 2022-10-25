package com.pharmacy.data.source.local.orders.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Order
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity
import java.util.*

object OrderEntityToOrderConverter : ModelConverter<OrderEntity, Order> {
    override fun convert(value: OrderEntity): Order {
        return Order(
            id = value.id,
            date = Date(value.date),
            status = Order.Status.valueOf(value.status)
        )
    }
}

object OrderToOrderEntityConverter : ModelConverter<Order, OrderEntity> {
    override fun convert(value: Order): OrderEntity {
        return OrderEntity(
            id = value.id,
            date = value.date.time,
            status = value.status.name
        )
    }
}

object OrderEntityTransformer : ModelTransformer<Order, OrderEntity> {
    override val source = OrderToOrderEntityConverter
    override val receiver = OrderEntityToOrderConverter
}