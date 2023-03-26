package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.Empty
import com.pharmacy.common.extensions.Zero
import com.pharmacy.data.model.Product
import com.pharmacy.ui.model.converter.ProductItemToProductConverter
import com.pharmacy.ui.model.converter.ProductItemTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String,
    val categories: List<CategoryItem>,
    val price: PriceItem,
) : Parcelable, ModelConverter<ProductItem, Product> by ProductItemToProductConverter {
    companion object : ModelTransformer<Product, ProductItem> by ProductItemTransformer {
        val Empty: ProductItem = ProductItem(
            id = Int.Zero,
            name = String.Empty,
            imageUrl = String.Empty,
            description = String.Empty,
            categories = emptyList(),
            price = PriceItem.Empty
        )
    }
}
