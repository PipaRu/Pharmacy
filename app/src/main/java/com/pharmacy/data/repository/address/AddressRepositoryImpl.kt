package com.pharmacy.data.repository.address

import com.pharmacy.data.model.Address
import com.pharmacy.data.source.remote.address.AddressRemoteDataSource
import kotlinx.coroutines.flow.Flow

class AddressRepositoryImpl(
    private val remoteDataSource: AddressRemoteDataSource,
) : AddressRepository {

    override fun search(query: String): Flow<List<Address>> {
        return remoteDataSource.search(query)
    }

}