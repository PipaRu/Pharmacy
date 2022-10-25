package com.pharmacy.common.ui.compose.text.field

import androidx.compose.ui.text.input.VisualTransformation
import com.pharmacy.common.formatter.PhoneFormatter

object VisualTransformations {
    val phoneNumber: VisualTransformation = PhoneNumberVisualTransformation(
        mask = PhoneFormatter.MASK,
        numberIndicator = PhoneFormatter.MASK_NUMBER_INDICATOR
    )
}