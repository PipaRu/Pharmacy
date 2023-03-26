package com.pharmacy.ui.screen.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.repository.basket.BasketRepository
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.product_details.model.mvi.ProductDetailsSideEffect
import com.pharmacy.ui.screen.product_details.model.mvi.ProductDetailsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container

class ProductDetailsViewModel(
    private val productsRepository: ProductsRepository,
    private val basketRepository: BasketRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<ProductDetailsViewState, ProductDetailsSideEffect> {

    override val container = container<ProductDetailsViewState, ProductDetailsSideEffect>(
        initialState = ProductDetailsViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(ProductDetailsSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    fun setProduct(productId: Int) = intent {
        repeatOnSubscription {
            reduce { state.copy(isContentLoading = true) }
            val productFlow = productsRepository.allProducts
                .map { products ->
                    products.find { product -> product.id == productId }
                        ?.let(ProductItem.Companion::from)
                        ?: throw IllegalStateException("Product not found")
                }
                .onEach {
                    reduce {
                        state.copy(
                            product = it
                        )
                    }
                }
            val basketBunchFlow = basketRepository.getAll().map { list ->
                list.find { it.product.id == productId }
                    ?.let { BasketBunchItem.from(it) }
            }
            combine(productFlow, basketBunchFlow) { product, basket ->
                reduce {
                    state.copy(
                        isContentLoading = false,
                        product = product,
                        basketBunch = basket,
                    )
                }
            }
                .collect()
        }
    }

    fun back() = intent {
        postSideEffect(ProductDetailsSideEffect.NavigateBack)
    }

    fun addToBasket() = intent {
        val basketBunch = state.basketBunch
        if (basketBunch == null) {
            basketRepository.add(BasketBunch(state.product.convert(), 1))
        } else {
            basketRepository.update(basketBunch.copy(count = basketBunch.count.inc()).convert())
        }
            .flowOn(Dispatchers.Default)
            .collect()
    }

    fun deleteFromBasket() = intent {
        val basketBunch = state.basketBunch
        if (basketBunch == null) {
            return@intent
        } else if (basketBunch.count == 1) {
            basketRepository.deleteById(basketBunch.id)
        } else {
            basketRepository.update(basketBunch.copy(count = basketBunch.count.dec()).convert())
        }
            .flowOn(Dispatchers.Default)
            .collect()
    }

}