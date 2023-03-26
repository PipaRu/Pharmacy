package com.pharmacy.data.source.remote.model

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.data.source.remote.model.converter.CategoryDtoToCategoryConverter
import com.pharmacy.data.source.remote.model.converter.CategoryDtoTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("name")
    val name: String?,
) : ModelConverter<CategoryDto, Category> by CategoryDtoToCategoryConverter {
    companion object : ModelTransformer<Category, CategoryDto> by CategoryDtoTransformer
}