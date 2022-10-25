package com.pharmacy.data.source.remote.address

import com.pharmacy.data.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRemoteDataSource {

    fun search(query: String): Flow<List<Address>>

}