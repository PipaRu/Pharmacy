package com.pharmacy.data.source.local.basket.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.data.source.local.basket.db.enitity.CategoryEntity

object CategoryEntityToCategoryConverter : ModelConverter<CategoryEntity, Category> {
    override fun convert(value: CategoryEntity): Category = Category(
        name = value.name,
    )
}

object CategoryToCategoryEntityConverter : ModelConverter<Category, CategoryEntity> {
    override fun convert(value: Category): CategoryEntity = CategoryEntity(
        name = value.name,
    )
}

object CategoryEntityTransformer : ModelTransformer<Category, CategoryEntity> {
    override val source = CategoryToCategoryEntityConverter
    override val receiver = CategoryEntityToCategoryConverter
}