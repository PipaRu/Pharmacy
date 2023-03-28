package com.pharmacy.ui.screen.showcase.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.pharmacy.common.extensions.Zero
import com.pharmacy.ui.model.ProductItem
import kotlinx.parcelize.Parcelize

@Stable
sealed class ShowcaseItem(
    open val id: Int,
) : Parcelable {

    @Stable
    @Parcelize
    data class Product(
        override val id: Int,
        val item: ProductItem,
        val inBasket: Boolean,
        val inBasketCount: Int,
        val isBasketLoading: Boolean,
    ) : ShowcaseItem(id) {
        companion object {
            val Empty: Product = Product(
                id = Int.Zero,
                item = ProductItem.Empty,
                inBasket = false,
                inBasketCount = 0,
                isBasketLoading = false
            )
        }
    }

    @Stable
    @Parcelize
    data class ProductPlaceHolder(override val id: Int) : ShowcaseItem(id)

    @Stable
    @Parcelize
    object Loader : ShowcaseItem(-1)

    @Stable
    @Parcelize
    data class Error(val value: Throwable) : ShowcaseItem(-2)

}