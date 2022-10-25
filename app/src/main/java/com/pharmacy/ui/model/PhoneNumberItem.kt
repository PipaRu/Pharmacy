package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.converter.model.ModelConverter
import com.pharmacy.common.converter.model.ModelTransformer
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.data.model.PhoneNumber
import com.pharmacy.ui.model.converter.PhoneNumberItemToPhoneNumberConverter
import com.pharmacy.ui.model.converter.PhoneNumberItemTransformer
import kotlinx.parcelize.Parcelize
import com.pharmacy.common.model.PhoneNumber as CommonPhoneNumber

@Parcelize
data class PhoneNumberItem(
    override val countryCode: String = CommonPhoneNumber.CountryCode.DEFAULT,
    override val number: String = emptyString(),
) : Parcelable,
    CommonPhoneNumber,
    ModelConverter<PhoneNumberItem, PhoneNumber> by PhoneNumberItemToPhoneNumberConverter {

    companion object : ModelTransformer<PhoneNumber, PhoneNumberItem> by PhoneNumberItemTransformer

}