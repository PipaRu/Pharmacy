package com.pharmacy.data.source.local.basket.db.enitity.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import com.pharmacy.data.source.local.basket.db.enitity.ProductEntity

object BasketBunchEntityToBasketBunchConverter : ModelConverter<BasketBunchEntity, BasketBunch> {
    override fun convert(value: BasketBunchEntity): BasketBunch {
        return BasketBunch(
            id = value.id,
            product = value.product.convert(),
            count = value.count
        )
    }
}

object BasketBunchToBasketBunchEntityConverter : ModelConverter<BasketBunch, BasketBunchEntity> {
    override fun convert(value: BasketBunch): BasketBunchEntity {
        return BasketBunchEntity(
            id = value.id,
            product = ProductEntity.from(value.product),
            count = value.count
        )
    }
}

object BasketBunchEntityTransformer : ModelTransformer<BasketBunch, BasketBunchEntity> {
    override val source = BasketBunchToBasketBunchEntityConverter
    override val receiver = BasketBunchEntityToBasketBunchConverter
}