package com.pharmacy.data.source.local.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.Address
import com.pharmacy.data.model.PhoneNumber
import com.pharmacy.data.model.Profile
import kotlinx.coroutines.flow.Flow

class AuthLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences,
) : AuthLocalDataSource {

    companion object {
        private const val KEY_PROFILE_ID = "user_id"
        private const val KEY_PROFILE_NAME = "user_name"
        private const val KEY_PROFILE_EMAIL = "user_email"
        private const val KEY_PROFILE_PHONE_COUNTRY_CODE = "user_phone_country_code"
        private const val KEY_PROFILE_PHONE_NUMBER = "user_phone_number"
        private const val KEY_PROFILE_ADDRESS_ID = "user_address_id"
        private const val KEY_PROFILE_ADDRESS_NAME = "user_address_name"
    }

    override fun saveUser(profile: Profile): Flow<Unit> = flowOf {
        sharedPreferences.edit(commit = true) {
            putInt(KEY_PROFILE_ID, profile.id)
            putString(KEY_PROFILE_NAME, profile.name)
            putString(KEY_PROFILE_EMAIL, profile.email)
            putString(KEY_PROFILE_PHONE_COUNTRY_CODE, profile.phone.countryCode)
            putString(KEY_PROFILE_PHONE_NUMBER, profile.phone.number)
            if (profile.address == null) {
                remove(KEY_PROFILE_ADDRESS_ID)
                remove(KEY_PROFILE_ADDRESS_NAME)
            } else {
                putLong(KEY_PROFILE_ADDRESS_ID, profile.address.id)
                putString(KEY_PROFILE_ADDRESS_NAME, profile.address.name)
            }
        }
    }

    override fun getUser(): Flow<Profile> = flowOf {
        sharedPreferences.run {
            val id = getInt(KEY_PROFILE_ID, 0)
            val name = getString(KEY_PROFILE_NAME, null).orEmpty()
            val email = getString(KEY_PROFILE_EMAIL, null).orEmpty()
            val phoneCountryCode = getString(KEY_PROFILE_PHONE_COUNTRY_CODE, null).orEmpty()
            val phoneNumber = getString(KEY_PROFILE_PHONE_NUMBER, null).orEmpty()
            val addressId = getLong(KEY_PROFILE_ADDRESS_ID, -1L)
            val addressName = getString(KEY_PROFILE_ADDRESS_NAME, null).orEmpty()
            val address = if (addressId != -1L) {
                Address(
                    id = addressId,
                    name = addressName
                )
            } else {
                null
            }
            Profile(
                id = id,
                name = name,
                email = email,
                phone = PhoneNumber(
                    countryCode = phoneCountryCode,
                    number = phoneNumber
                ),
                address = address
            )
        }
    }

    override fun deleteUser(): Flow<Unit> = flowOf {
        sharedPreferences.edit(commit = true) {
            remove(KEY_PROFILE_ID)
            remove(KEY_PROFILE_NAME)
            remove(KEY_PROFILE_EMAIL)
            remove(KEY_PROFILE_PHONE_COUNTRY_CODE)
            remove(KEY_PROFILE_PHONE_NUMBER)
            remove(KEY_PROFILE_ADDRESS_ID)
            remove(KEY_PROFILE_ADDRESS_NAME)
        }
    }

}