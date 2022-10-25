package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Order
import com.pharmacy.ui.model.OrderItem

object OrderItemToOrderConverter : ModelConverter<OrderItem, Order> {
    override fun convert(value: OrderItem): Order {
        return Order(
            id = value.id,
            date = value.date,
            status = Order.Status.valueOf(value.status.name)
        )
    }
}

object OrderToOrderItemConverter : ModelConverter<Order, OrderItem> {
    override fun convert(value: Order): OrderItem {
        return OrderItem(
            id = value.id,
            date = value.date,
            status = OrderItem.Status.valueOf(value.status.name)
        )
    }
}

object OrderItemTransformer : ModelTransformer<Order, OrderItem> {
    override val source = OrderToOrderItemConverter
    override val receiver = OrderItemToOrderConverter
}