package com.pharmacy.ui.screen.admin_product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.extensions.delay
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.repository.admin_products.AdminProductsRepository
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsSideEffect
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AdminProductDetailsViewModel(
    private val productsRepository: AdminProductsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminProductDetailsViewState, AdminProductDetailsSideEffect> {

    override val container = container<AdminProductDetailsViewState, AdminProductDetailsSideEffect>(
        initialState = AdminProductDetailsViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(AdminProductDetailsSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    fun setProduct(product: ProductItem) = intent {
        reduce { state.copy(isContentLoading = false, product = product) }
    }

    fun back() = intent {
        postSideEffect(AdminProductDetailsSideEffect.NavigateBack)
    }

    fun update(product: ProductItem) = intent {
        reduce { state.copy(isContentEditingLoading = true) }
        productsRepository.updateProducts(setOf(product.convert()))
            .flowOn(Dispatchers.IO)
            .delay(MockDelay.MEDIUM)
            .collect()
        reduce { state.copy(product = product, isContentEditingLoading = false) }
    }

    fun deleteAction() = intent {
        postSideEffect(AdminProductDetailsSideEffect.ShowDeletionProductWarning)
    }

    fun delete() = intent {
        val product = state.product
        reduce { state.copy(isContentDeletionLoading = true) }
        productsRepository.deleteProducts(setOf(product.convert()))
            .flowOn(Dispatchers.IO)
            .delay(MockDelay.MEDIUM)
            .collect()
        reduce { state.copy(isContentDeletionLoading = false) }
        postSideEffect(AdminProductDetailsSideEffect.NavigateBack)
    }

}