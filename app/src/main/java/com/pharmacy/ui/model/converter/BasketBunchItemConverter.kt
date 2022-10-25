package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.ProductItem

object BasketBunchItemToBasketBunchConverter : ModelConverter<BasketBunchItem, BasketBunch> {
    override fun convert(value: BasketBunchItem): BasketBunch {
        return BasketBunch(
            id = value.id,
            product = value.product.convert(),
            count = value.count
        )
    }
}

object BasketBunchToBasketBunchItemConverter : ModelConverter<BasketBunch, BasketBunchItem> {
    override fun convert(value: BasketBunch): BasketBunchItem {
        return BasketBunchItem(
            id = value.id,
            product = ProductItem.from(value.product),
            count = value.count
        )
    }
}

object BasketBunchItemTransformer : ModelTransformer<BasketBunch, BasketBunchItem> {
    override val source = BasketBunchToBasketBunchItemConverter
    override val receiver = BasketBunchItemToBasketBunchConverter
}