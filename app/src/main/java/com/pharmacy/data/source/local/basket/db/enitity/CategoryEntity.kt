package com.pharmacy.data.source.local.basket.db.enitity

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.data.source.local.basket.db.enitity.converter.CategoryEntityToCategoryConverter
import com.pharmacy.data.source.local.basket.db.enitity.converter.CategoryEntityTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    @SerialName("name")
    val name: String,
) : ModelConverter<CategoryEntity, Category> by CategoryEntityToCategoryConverter {
    companion object : ModelTransformer<Category, CategoryEntity> by CategoryEntityTransformer
}