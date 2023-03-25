package com.pharmacy.ui.dialog.authorization_required

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.formatter.PhoneFormatter
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.UserStatus
import com.pharmacy.data.repository.auth.AuthRepository
import com.pharmacy.ui.dialog.authorization_required.mvi.AuthorizationRequiredSideEffect
import com.pharmacy.ui.dialog.authorization_required.mvi.AuthorizationRequiredViewState
import com.pharmacy.ui.model.PhoneNumberItem
import com.pharmacy.ui.model.UserStatusItem
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

class AuthorizationRequiredDialogViewModel(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    ContainerHost<AuthorizationRequiredViewState, AuthorizationRequiredSideEffect> {

    override val container =
        container<AuthorizationRequiredViewState, AuthorizationRequiredSideEffect>(
            initialState = AuthorizationRequiredViewState(),
            savedStateHandle = savedStateHandle,
            settings = Container.Settings(
                exceptionHandler = Crashlytics.coroutineExceptionHandler {

                }
            )
        )

    init {
        intent {
            repeatOnSubscription {
                authRepository.userStatus.onEach { userStatus ->
                    when (userStatus) {
                        is UserStatus.Admin,
                        is UserStatus.User -> {
                            postSideEffect(AuthorizationRequiredSideEffect.OnLoginSuccess)
                        }
                        is UserStatus.Unauthorized -> {
                            val status = UserStatusItem.from(userStatus)
                            reduce { state.copy(status = status) }
                        }
                    }
                }
                    .flowOn(Dispatchers.IO)
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


    private fun isNameCorrect(name: String): Boolean {
        return name.length > 1
    }

    private fun isPhoneNumberCorrect(phone: PhoneNumberItem): Boolean {
        return phone.number.length == PhoneFormatter.MAX_NUMBERS
    }

}