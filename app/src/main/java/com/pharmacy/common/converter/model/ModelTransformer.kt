package com.pharmacy.common.converter.model

interface ModelTransformer<Original, Transformed> {

    val source: ModelConverter<Original, Transformed>

    val receiver: ModelConverter<Transformed, Original>

    fun Original.transform(): Transformed = source.convert(this)

    fun Transformed.retrieve(): Original = receiver.convert(this)

}