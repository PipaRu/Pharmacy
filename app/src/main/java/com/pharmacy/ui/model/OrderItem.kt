package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Order
import com.pharmacy.ui.model.converter.OrderItemToOrderConverter
import com.pharmacy.ui.model.converter.OrderItemTransformer
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OrderItem(
    val id: Long,
    val date: Date,
    val status: Status,
) : Parcelable, ModelConverter<OrderItem, Order> by OrderItemToOrderConverter {

    companion object : ModelTransformer<Order, OrderItem> by OrderItemTransformer

    enum class Status {
        UNDEFINED,
        CREATED,
        PROGRESS,
        COMPLETED,
        CANCELLED,
        FAILED,
    }
}