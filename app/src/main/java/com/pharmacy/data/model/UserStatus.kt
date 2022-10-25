package com.pharmacy.data.model

sealed class UserStatus {
    object Unauthorized : UserStatus()
    data class User(val profile: Profile) : UserStatus()
    data class Admin(val profile: Profile) : UserStatus()
}