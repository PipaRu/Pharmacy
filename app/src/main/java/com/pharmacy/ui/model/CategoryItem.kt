package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.Category
import com.pharmacy.ui.model.converter.CategoryItemToCategoryConverter
import com.pharmacy.ui.model.converter.CategoryItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val name: String,
) : Parcelable, ModelConverter<CategoryItem, Category> by CategoryItemToCategoryConverter {
    companion object : ModelTransformer<Category, CategoryItem> by CategoryItemTransformer
}