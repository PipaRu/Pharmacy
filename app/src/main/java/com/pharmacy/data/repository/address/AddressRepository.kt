package com.pharmacy.data.repository.address

import com.pharmacy.data.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {

    fun search(query: String): Flow<List<Address>>

}