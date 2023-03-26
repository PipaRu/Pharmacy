package com.pharmacy.data.source.local.basket.db.enitity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Product
import com.pharmacy.data.source.local.basket.db.enitity.converter.ProductEntityToProductConverter
import com.pharmacy.data.source.local.basket.db.enitity.converter.ProductEntityTransformer

data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "categories")
    val categories: List<CategoryEntity>,
    @Embedded(prefix = "price_")
    val price: PriceEntity,
) : ModelConverter<ProductEntity, Product> by ProductEntityToProductConverter {
    companion object : ModelTransformer<Product, ProductEntity> by ProductEntityTransformer
}