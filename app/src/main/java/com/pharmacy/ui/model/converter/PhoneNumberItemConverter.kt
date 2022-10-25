package com.pharmacy.ui.model.converter

import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.data.model.PhoneNumber
import com.pharmacy.ui.model.PhoneNumberItem

object PhoneNumberItemToPhoneNumberConverter : ModelConverter<PhoneNumberItem, PhoneNumber> {
    override fun convert(value: PhoneNumberItem): PhoneNumber {
        return PhoneNumber(
            countryCode = value.countryCode,
            number = value.number
        )
    }
}

object PhoneNumberToPhoneNumberItemConverter : ModelConverter<PhoneNumber, PhoneNumberItem> {
    override fun convert(value: PhoneNumber): PhoneNumberItem {
        return PhoneNumberItem(
            countryCode = value.countryCode,
            number = value.number
        )
    }
}

object PhoneNumberItemTransformer : ModelTransformer<PhoneNumber, PhoneNumberItem> {
    override val source = PhoneNumberToPhoneNumberItemConverter
    override val receiver = PhoneNumberItemToPhoneNumberConverter
}