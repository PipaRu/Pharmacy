package com.pharmacy.data.source.local.auth

import com.pharmacy.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {

    fun saveUser(profile: Profile): Flow<Unit>

    fun getUser(): Flow<Profile>

    fun deleteUser(): Flow<Unit>

}