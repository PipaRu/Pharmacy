package com.pharmacy.ui.screen.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.model.Profile
import com.pharmacy.data.model.UserStatus
import com.pharmacy.data.repository.auth.AuthRepository
import com.pharmacy.data.repository.basket.BasketRepository
import com.pharmacy.data.repository.order.OrdersRepository
import com.pharmacy.ui.model.AddressItem
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.OrderItem
import com.pharmacy.ui.model.ProfileItem
import com.pharmacy.ui.screen.checkout.model.mvi.CheckoutSideEffect
import com.pharmacy.ui.screen.checkout.model.mvi.CheckoutViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class CheckoutViewModel(
    private val basketRepository: BasketRepository,
    private val authRepository: AuthRepository,
    private val ordersRepository: OrdersRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<CheckoutViewState, CheckoutSideEffect> {

    override val container = container<CheckoutViewState, CheckoutSideEffect>(
        initialState = CheckoutViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(CheckoutSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    private val products = MutableStateFlow<List<BasketBunchItem>?>(null)

    init {
        val userStatusFlow = authRepository.userStatus
            .flowOn(Dispatchers.Default)
        val productsFlow = products
            .filterNotNull()
            .flowOn(Dispatchers.Default)
        combine(userStatusFlow, productsFlow, container.stateFlow) { userStatus, products, state ->
            val profile: Profile? = when (userStatus) {
                is UserStatus.Admin -> userStatus.profile
                is UserStatus.User -> userStatus.profile
                is UserStatus.Unauthorized -> null
            }
            state.copy(
                profile = profile?.let(ProfileItem.Companion::from),
                products = products,
                isCheckoutAvailable = profile?.address != null,
                isContentLoading = false
            )
        }
            .onEach { newState -> intent { reduce { newState } } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun back() = intent { postSideEffect(CheckoutSideEffect.NavigateBack) }

    fun toLogin() = intent { postSideEffect(CheckoutSideEffect.NavigateToLogin) }

    fun addressSelection() = intent { postSideEffect(CheckoutSideEffect.AddressSelection) }

    fun setAddress(address: AddressItem) = intent {
        authRepository.updateUserAddress(address = address.convert())
            .flowOn(Dispatchers.IO)
            .collect()
    }

    fun setProducts(products: List<BasketBunchItem>) = intent {
        this@CheckoutViewModel.products.emit(products)
    }

    fun checkout() = intent {
        reduce { state.copy(isCheckoutProcessLoading = true) }
        delay(MockDelay.LONG)
        val products = state.products.map(BasketBunchItem::convert)
        val address = requireNotNull(state.profile?.address).convert()
        val order = ordersRepository.createOrder(products, address)
            .flowOn(Dispatchers.IO)
            .first()
            .let(OrderItem.Companion::from)
        val productIds = products.map(BasketBunch::id).toSet()
        basketRepository.deleteAllById(productIds)
            .flowOn(Dispatchers.IO)
            .collect()
        reduce { state.copy(isCheckoutProcessLoading = false, order = order) }
    }

    fun nextAfterCheckout() = intent {
        postSideEffect(CheckoutSideEffect.NavigateNextAfterCheckout)
    }

}