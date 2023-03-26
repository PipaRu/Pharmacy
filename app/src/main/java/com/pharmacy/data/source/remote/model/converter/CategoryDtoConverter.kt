package com.pharmacy.data.source.remote.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.data.source.remote.model.CategoryDto

object CategoryDtoToCategoryConverter : ModelConverter<CategoryDto, Category> {
    override fun convert(value: CategoryDto): Category = Category(
        name = value.name.orEmpty(),
    )
}

object CategoryToCategoryDtoConverter : ModelConverter<Category, CategoryDto> {
    override fun convert(value: Category): CategoryDto = CategoryDto(
        name = value.name,
    )
}

object CategoryDtoTransformer : ModelTransformer<Category, CategoryDto> {
    override val source = CategoryToCategoryDtoConverter
    override val receiver = CategoryDtoToCategoryConverter
}