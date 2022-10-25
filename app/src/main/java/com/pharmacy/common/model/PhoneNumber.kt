package com.pharmacy.common.model

interface PhoneNumber {

    val countryCode: String

    val number: String

    object CountryCode {
        val RU = "7"
        val DEFAULT: String = RU
    }

}