@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pharmacy.ui.dialog.address_picker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.data.repository.address.AddressRepository
import com.pharmacy.ui.dialog.address_picker.model.mvi.AddressPickerSideEffect
import com.pharmacy.ui.dialog.address_picker.model.mvi.AddressPickerViewState
import com.pharmacy.ui.model.AddressItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AddressPickerViewModel(
    private val addressRepository: AddressRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AddressPickerViewState, AddressPickerSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE = 1_000L
    }

    override val container = container<AddressPickerViewState, AddressPickerSideEffect>(
        initialState = AddressPickerViewState(),
        savedStateHandle = savedStateHandle
    )

    private val queryFlow = QueryFlow(
        debounceMilliseconds = QUERY_DEBOUNCE,
        coroutineScope = viewModelScope
    )

    init {
        queryFlow.pending
            .distinctUntilChanged { old, new -> old == new }
            .onEach {
                intent {
                    reduce { state.copy(isQueryLoading = true, addresses = emptyList()) }
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
        queryFlow
            .flatMapLatest { query -> addressRepository.search(query) }
            .flowOn(Dispatchers.IO)
            .onEach { addresses ->
                intent {
                    reduce {
                        state.copy(
                            isQueryLoading = false,
                            addresses = addresses.map(AddressItem.Companion::from)
                        )
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun search(query: String) = intent {
        queryFlow.query(query)
        reduce { state.copy(query = query) }
    }

    fun select(address: AddressItem) = intent {
        postSideEffect(AddressPickerSideEffect.AddressSelected(address))
    }

}