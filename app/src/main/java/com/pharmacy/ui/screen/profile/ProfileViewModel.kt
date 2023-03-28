package com.pharmacy.ui.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.formatter.PhoneFormatter
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.repository.admin.AdminRepository
import com.pharmacy.data.repository.auth.AuthRepository
import com.pharmacy.ui.model.AddressItem
import com.pharmacy.ui.model.PhoneNumberItem
import com.pharmacy.ui.model.UserStatusItem
import com.pharmacy.ui.screen.profile.model.mvi.ProfileSideEffect
import com.pharmacy.ui.screen.profile.model.mvi.ProfileViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val adminRepository: AdminRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<ProfileViewState, ProfileSideEffect> {

    override val container = container<ProfileViewState, ProfileSideEffect>(
        initialState = ProfileViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(ProfileSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    init {
        intent {
            repeatOnSubscription {
                authRepository.userStatus.onEach { userStatus ->
                    val status = UserStatusItem.from(userStatus)
                    reduce { state.copy(status = status) }
                    postSideEffect(ProfileSideEffect.DropNavigation)
                }
                    .flowOn(Dispatchers.Default)
                    .collect()
            }
        }

    }

    fun login(name: String, phone: PhoneNumberItem) = intent {
        reduce { state.copy(isLoading = true) }
        delay(MockDelay.LONG)
        authRepository.authorize(name = name, phone = phone.convert())
            .flowOn(Dispatchers.IO)
            .collect()
        reduce {
            state.copy(isLoading = false)
        }
    }

    fun logout() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        authRepository.logout().flowOn(Dispatchers.IO).collect()
        reduce {
            state.copy(isLoading = false)
        }
    }

    fun onProfileSettings() = intent {
        postSideEffect(ProfileSideEffect.ShowContentInDeveloping())
    }

    fun onFavorites() = intent {
        postSideEffect(ProfileSideEffect.ShowContentInDeveloping())
    }

    fun onOrders() = intent {
        postSideEffect(ProfileSideEffect.NavigateToOrders)
    }

    fun onSettings() = intent {
        postSideEffect(ProfileSideEffect.ShowContentInDeveloping())
    }

    fun onAdministration() = intent {
        postSideEffect(ProfileSideEffect.NavigateToAdministration)
    }

    fun onUnauthorizedDataChange(name: String, phone: PhoneNumberItem) = intent {
        reduce {
            state.copy(
                status = UserStatusItem.Unauthorized(
                    name = name,
                    phone = phone,
                    isLoginAvailable = isNameCorrect(name) && isPhoneNumberCorrect(phone)
                )
            )
        }
    }

    fun selectAddress() = intent {
        postSideEffect(ProfileSideEffect.SelectAddress)
    }

    fun deleteAddress() = intent {
        authRepository.updateUserAddress(address = null)
            .flowOn(Dispatchers.IO)
            .collect()
    }

    fun setAddress(address: AddressItem) = intent {
        authRepository.updateUserAddress(address = address.convert())
            .flowOn(Dispatchers.IO)
            .collect()
    }

    private fun isNameCorrect(name: String): Boolean {
        return name.length > 1
    }

    private fun isPhoneNumberCorrect(phone: PhoneNumberItem): Boolean {
        return phone.number.length == PhoneFormatter.MAX_NUMBERS
    }

}