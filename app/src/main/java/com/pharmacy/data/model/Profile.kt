package com.pharmacy.data.model

data class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val phone: PhoneNumber,
    val address: Address?,
)