package com.pharmacy.ui.screen.admin_products

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.extensions.showWithLifecycle
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.databinding.FragmentAdminProductsBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.admin_products.content.AdminProductsScreenContent
import com.pharmacy.ui.screen.admin_products.model.mvi.AdminProductsSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe
import com.pharmacy.R.string.content_profile_admin_products_dialog_delete_warning_action_negative as dialog_delete_warning_action_negative
import com.pharmacy.R.string.content_profile_admin_products_dialog_delete_warning_action_positive as dialog_delete_warning_action_positive
import com.pharmacy.R.string.content_profile_admin_products_dialog_delete_warning_message as dialog_delete_warning_message

class AdminProductsFragment : AppFragment(R.layout.fragment_admin_products) {

    private val viewModel by stateViewModel<AdminProductsViewModel>()
    private val viewBinding by viewBinding(FragmentAdminProductsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { AdminProductsScreenContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: AdminProductsSideEffect) = when (effect) {
        is AdminProductsSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is AdminProductsSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
        is AdminProductsSideEffect.NavigateBack -> navigateBack()
        is AdminProductsSideEffect.NavigateToProductDetails -> navigateToProductDetails(effect.productItem)
        is AdminProductsSideEffect.ShowDeleteAllSelectedProductsWarning -> showDeleteAllSelectedProductsWarning()
    }

    private fun showDeleteAllSelectedProductsWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(dialog_delete_warning_message))
            .setPositiveButton(dialog_delete_warning_action_positive) { _, _ ->
                viewModel.deleteAllSelected()
            }
            .setNegativeButton(dialog_delete_warning_action_negative, null)
            .create()
            .showWithLifecycle(viewLifecycleOwner)
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun navigateToProductDetails(product: ProductItem) {
        val dir = AdminProductsFragmentDirections
            .actionAdminProductsFragmentToAdminProductDetailsFragment(product = product)
        findNavController().navigate(dir)
    }

    private fun showContentInDeveloping(contentName: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(InDevelopingDialogContent(contentName))
        findNavController().navigate(dir)
    }

    private fun showSomethingWentWrong(target: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(SomethingWentWrong(target))
        findNavController().navigate(dir)
    }

}