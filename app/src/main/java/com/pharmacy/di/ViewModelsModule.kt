package com.pharmacy.di

import com.pharmacy.ui.dialog.address_picker.AddressPickerViewModel
import com.pharmacy.ui.screen.admin_product_details.AdminProductDetailsViewModel
import com.pharmacy.ui.screen.admin_products.AdminProductsViewModel
import com.pharmacy.ui.screen.basket.BasketViewModel
import com.pharmacy.ui.screen.checkout.CheckoutViewModel
import com.pharmacy.ui.screen.orders.OrdersViewModel
import com.pharmacy.ui.screen.profile.ProfileViewModel
import com.pharmacy.ui.screen.showcase.ShowcaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { parameters ->
        BasketViewModel(
            basketRepository = get(),
            authRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        ProfileViewModel(
            authRepository = get(),
            adminRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        ShowcaseViewModel(
            productsRepository = get(),
            basketRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        com.pharmacy.ui.screen.admin_menu.AdminMenuViewModel(
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        AdminProductsViewModel(
            productsRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        AdminProductDetailsViewModel(
            productsRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        AddressPickerViewModel(
            addressRepository = get(),
            savedStateHandle = parameters.get()
        )
    }

    viewModel { parameters ->
        CheckoutViewModel(
            authRepository = get(),
            basketRepository = get(),
            ordersRepository = get(),
            savedStateHandle = parameters.get(),
        )
    }

    viewModel { parameters ->
        OrdersViewModel(
            ordersRepository = get(),
            savedStateHandle = parameters.get(),
        )
    }

}