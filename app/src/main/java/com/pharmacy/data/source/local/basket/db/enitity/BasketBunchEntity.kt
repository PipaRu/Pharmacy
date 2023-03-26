package com.pharmacy.data.source.local.basket.db.enitity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.source.local.basket.db.enitity.converter.BasketBunchEntityToBasketBunchConverter
import com.pharmacy.data.source.local.basket.db.enitity.converter.BasketBunchEntityTransformer

@Entity(tableName = "basket")
data class BasketBunchEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @Embedded(prefix = "product_")
    val product: ProductEntity, // TODO: Незачем хранить, достаточно айдишника
    @ColumnInfo(name = "count")
    val count: Int,
) : ModelConverter<BasketBunchEntity, BasketBunch> by BasketBunchEntityToBasketBunchConverter {
    companion object : ModelTransformer<BasketBunch, BasketBunchEntity> by BasketBunchEntityTransformer
}