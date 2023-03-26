package com.pharmacy.data.source.local.db.adapters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pharmacy.common.converter.json.JsonConverter
import com.pharmacy.data.source.local.basket.db.enitity.CategoryEntity
import kotlin.reflect.typeOf

@ProvidedTypeConverter
class CategoryEntityListConverter(
    private val jsonConverter: JsonConverter,
) {

    @TypeConverter
    fun listToString(list: List<CategoryEntity>): String {
        return jsonConverter.serialize(list, typeOf<List<CategoryEntity>>())
    }

    @TypeConverter
    fun stringToList(string: String): List<CategoryEntity> {
        return jsonConverter.deserialize(string, typeOf<List<CategoryEntity>>())
    }

}