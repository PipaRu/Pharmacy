package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.ui.model.CategoryItem

object CategoryItemToCategoryConverter : ModelConverter<CategoryItem, Category> {
    override fun convert(value: CategoryItem): Category {
        return Category(
            name = value.name,
        )
    }
}

object CategoryToCategoryItemConverter : ModelConverter<Category, CategoryItem> {
    override fun convert(value: Category): CategoryItem {
        return CategoryItem(
            name = value.name,
        )
    }
}

object CategoryItemTransformer : ModelTransformer<Category, CategoryItem> {
    override val source = CategoryToCategoryItemConverter
    override val receiver = CategoryItemToCategoryConverter
}