package com.pharmacy.common.ui.compose.text.field

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.pharmacy.common.extensions.filterNumbers
import com.pharmacy.common.extensions.trim

class PhoneNumberVisualTransformation(
    private val mask: String,
    private val numberIndicator: Char
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val filtered = text.filterNumbers().trim(mask.count { it == numberIndicator })

        var maskText = mask
        filtered.forEach { number ->
            maskText = maskText.replaceFirst(numberIndicator, number, true)
        }

        val annotatedString = AnnotatedString.Builder().apply {
            val indicatorCharStyle = SpanStyle(color = Color.LightGray)
            maskText.forEach { char ->
                if (char == numberIndicator) {
                    withStyle(indicatorCharStyle) { append(char) }
                } else {
                    append(char)
                }
            }
        }.toAnnotatedString()

        val phoneNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var i = 1
                val transformed = mask.indexOfFirst { char ->
                    if (char == numberIndicator) {
                        if (offset == i) true
                        else { i++; false }
                    } else {
                        false
                    }
                }.inc().coerceAtLeast(mask.indexOfFirst { it == numberIndicator })
                return transformed
            }

            override fun transformedToOriginal(offset: Int): Int {
                return mask.take(offset).filter { char -> char == numberIndicator }.length
            }
        }

        return TransformedText(annotatedString, phoneNumberOffsetTranslator)
    }

}