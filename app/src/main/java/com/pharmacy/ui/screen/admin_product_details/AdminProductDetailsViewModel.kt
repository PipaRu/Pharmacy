package com.pharmacy.ui.screen.admin_product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.delay
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.repository.admin_products.AdminProductsRepository
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsSideEffect
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AdminProductDetailsViewModel(
    private val productsRepository: ProductsRepository,
    private val adminProductsRepository: AdminProductsRepository,
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

    fun setProduct(product: ProductItem?) = intent {
        if (product == null) {
            val newProduct = adminProductsRepository.createProduct()
                .map(ProductItem.Companion::from)
                .single()
            reduce {
                state.copy(
                    isContentLoading = false,
                    product = newProduct,
                    isNewProduct = true,
                )
            }
        } else {
            reduce {
                state.copy(
                    isContentLoading = false,
                    product = product,
                )
            }
        }
        productsRepository.allProducts
            .onEach {
                val currentProduct = state.product
                val targetProduct = it.find { next -> next.id == currentProduct.id }
                    ?.let(ProductItem.Companion::from) ?: return@onEach

                if (targetProduct != currentProduct) {
                    reduce { state.copy(product = targetProduct) }
                }
            }
            .collect()
    }

    fun back() = intent {
        postSideEffect(AdminProductDetailsSideEffect.NavigateBack)
    }

    fun update(product: ProductItem) = intent {
        reduce { state.copy(isContentEditingLoading = true) }
        adminProductsRepository.updateProducts(setOf(product.convert()))
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
        adminProductsRepository.deleteProducts(setOf(product.convert()))
            .flowOn(Dispatchers.IO)
            .delay(MockDelay.MEDIUM)
            .collect()
        reduce { state.copy(isContentDeletionLoading = false) }
        postSideEffect(AdminProductDetailsSideEffect.NavigateBack)
    }

    fun changeCategoryName(
        product: ProductItem,
        categoryItem: CategoryItem,
        newName: String,
    ) = intent {
        val newProduct = product.copy(
            categories = product.categories.map { nextCategory ->
                if (nextCategory.name == categoryItem.name) {
                    nextCategory.copy(name = newName)
                } else {
                    nextCategory
                }
            }
        ).convert()
        adminProductsRepository.updateProducts(setOf(newProduct))
            .flowOn(Dispatchers.Main)
            .collect()
    }

    fun deleteCategory(
        product: ProductItem,
        categoryItem: CategoryItem,
    ) = intent {
        val newProduct = product.copy(
            categories = product.categories.mapNotNull { nextCategory ->
                if (nextCategory.name == categoryItem.name) {
                    null
                } else {
                    nextCategory
                }
            }
        ).convert()
        adminProductsRepository.updateProducts(setOf(newProduct))
            .flowOn(Dispatchers.Main)
            .collect()
    }

    fun addCategory(
        product: ProductItem,
        name: String,
    ) = intent {
        val newProduct = product.copy(
            categories = product.categories + CategoryItem(name),
        ).convert()
        adminProductsRepository.updateProducts(setOf(newProduct))
            .flowOn(Dispatchers.Main)
            .collect()
    }

}