package com.pharmacy.data.repository.auth

import com.pharmacy.data.model.Address
import com.pharmacy.data.model.PhoneNumber
import com.pharmacy.data.model.UserStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val userStatus: Flow<UserStatus>

    fun authorize(name: String, phone: PhoneNumber): Flow<Unit>

    fun registration(name: String, phone: PhoneNumber): Flow<Unit>

    fun logout(): Flow<Unit>

    fun updateUserAddress(address: Address?): Flow<UserStatus>

}