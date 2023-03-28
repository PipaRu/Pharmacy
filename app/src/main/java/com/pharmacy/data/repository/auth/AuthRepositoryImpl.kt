@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pharmacy.data.repository.auth

import com.pharmacy.common.extensions.emptyString
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.Address
import com.pharmacy.data.model.PhoneNumber
import com.pharmacy.data.model.Profile
import com.pharmacy.data.model.UserStatus
import com.pharmacy.data.source.local.auth.AuthLocalDataSource
import com.pharmacy.data.source.local.basket.BasketLocalDataSource
import com.pharmacy.data.source.local.db.DatabaseFactory
import com.pharmacy.data.source.local.orders.OrdersLocalDataSource
import com.pharmacy.di.DiComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.core.component.get

class AuthRepositoryImpl(
    private val localDataSource: AuthLocalDataSource,
) : AuthRepository {

    private val _userStatus = MutableStateFlow<UserStatus>(UserStatus.Unauthorized)

    override val userStatus: Flow<UserStatus>
        get() = _userStatus.asStateFlow().onStart {
            localDataSource.getUser()
                .map { profile ->
                    val status = if (profile.id == 0) {
                        DatabaseFactory.create()
                        UserStatus.Unauthorized
                    } else if (isAdmin(profile)) {
                        DatabaseFactory.create(profile.phone.number)
                        UserStatus.Admin(profile)
                    } else {
                        DatabaseFactory.create(profile.phone.number)
                        UserStatus.User(profile)
                    }
                    _userStatus.emit(status)
                }.collect()
        }

    override fun authorize(name: String, phone: PhoneNumber): Flow<Unit> {
        return createProfile(name, phone)
            .flatMapLatest { profile -> localDataSource.saveUser(profile).map { profile } }
            .onEach {
                // TODO: Костыль
                val basketLocalDataSource = DiComponent.get<BasketLocalDataSource>()
                val ordersLocalDataSource = DiComponent.get<OrdersLocalDataSource>()
                if (_userStatus.value == UserStatus.Unauthorized) {
                    val allBasket = basketLocalDataSource.getAll().first()
                    val allOrders = ordersLocalDataSource.getAll().first()
                    basketLocalDataSource.clear().collect()
                    ordersLocalDataSource.clear().collect()
                    DatabaseFactory.create(it.phone.number)
                    basketLocalDataSource.addAll(allBasket.toSet()).collect()
                    ordersLocalDataSource.addAll(allOrders.toSet()).collect()
                } else {
                    DatabaseFactory.create(it.name)
                }
                basketLocalDataSource.refresh().collect()
                ordersLocalDataSource.refresh().collect()
            }
            .map { profile ->
                if (isAdmin(profile)) {
                    _userStatus.emit(UserStatus.Admin(profile))
                } else {
                    _userStatus.emit(UserStatus.User(profile))
                }
            }
    }

    override fun registration(name: String, phone: PhoneNumber): Flow<Unit> {
        return authorize(name, phone)
    }

    override fun logout(): Flow<Unit> {
        return localDataSource.deleteUser()
            .onEach {
                // TODO: Костыль
                DatabaseFactory.create()
                val basketLocalDataSource = DiComponent.get<BasketLocalDataSource>()
                val ordersLocalDataSource = DiComponent.get<OrdersLocalDataSource>()
                basketLocalDataSource.clear().collect()
                ordersLocalDataSource.clear().collect()
                basketLocalDataSource.refresh().collect()
                ordersLocalDataSource.refresh().collect()
            }
            .onEach { _userStatus.emit(UserStatus.Unauthorized) }
    }

    override fun updateUserAddress(address: Address?): Flow<UserStatus> {
        return userStatus
            .flatMapLatest { status ->
                when (status) {
                    is UserStatus.Unauthorized -> flowOf(status)
                    is UserStatus.Admin -> {
                        flowOf { status.profile.copy(address = address) }
                            .flatMapLatest { profile -> localDataSource.saveUser(profile) }
                            .flatMapLatest { localDataSource.getUser() }
                            .map(status::copy)
                    }
                    is UserStatus.User -> {
                        flowOf { status.profile.copy(address = address) }
                            .flatMapLatest { profile -> localDataSource.saveUser(profile) }
                            .flatMapLatest { localDataSource.getUser() }
                            .map(status::copy)
                    }
                }
            }
            .onEach { status -> _userStatus.emit(status) }
            .take(1)
    }


    private fun createProfile(name: String, phone: PhoneNumber): Flow<Profile> = flowOf {
        Profile(
            id = 1,
            name = name,
            email = emptyString(),
            phone = phone,
            address = null
        )
    }

    private fun isAdmin(profile: Profile): Boolean {
        return profile.phone.number.all { it == '7' }
    }

}