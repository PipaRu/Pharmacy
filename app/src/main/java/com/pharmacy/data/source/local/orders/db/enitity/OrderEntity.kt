package com.pharmacy.data.source.local.orders.db.enitity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Order
import com.pharmacy.data.source.local.orders.db.enitity.converter.OrderEntityToOrderConverter
import com.pharmacy.data.source.local.orders.db.enitity.converter.OrderEntityTransformer

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "status")
    val status: String,
) : ModelConverter<OrderEntity, Order> by OrderEntityToOrderConverter {
    companion object : ModelTransformer<Order, OrderEntity> by OrderEntityTransformer
}