package com.pharmacy.ui.screen.admin_product_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.extensions.showWithLifecycle
import com.pharmacy.databinding.FragmentAdminProductDetailsBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.admin_product_details.content.AdminProductDetailsContent
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe
import com.pharmacy.R.string.content_profile_admin_product_details_dialog_delete_warning_action_negative as dialog_delete_warning_action_negative
import com.pharmacy.R.string.content_profile_admin_product_details_dialog_delete_warning_action_positive as dialog_delete_warning_action_positive

class AdminProductDetailsFragment : Fragment(R.layout.fragment_admin_product_details) {

    private val viewModel by stateViewModel<AdminProductDetailsViewModel>()
    private val viewBinding by viewBinding(FragmentAdminProductDetailsBinding::bind)
    private val args by navArgs<AdminProductDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setProduct(args.product)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { AdminProductDetailsContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: AdminProductDetailsSideEffect) = when (effect) {
        is AdminProductDetailsSideEffect.ShowContentInDeveloping -> {
            showContentInDeveloping(effect.contentName)
        }
        is AdminProductDetailsSideEffect.ShowSomethingWentWrong -> {
            showSomethingWentWrongDialog(effect.target)
        }
        is AdminProductDetailsSideEffect.NavigateBack -> {
            navigateBack()
        }
        is AdminProductDetailsSideEffect.ShowDeletionProductWarning -> {
            showDeletionProductWarning()
        }
    }

    private fun showDeletionProductWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.content_profile_admin_product_details_dialog_delete_warning_message)
            .setPositiveButton(dialog_delete_warning_action_positive) { _, _ -> viewModel.delete() }
            .setNegativeButton(dialog_delete_warning_action_negative) { _, _ -> }
            .create()
            .showWithLifecycle(viewLifecycleOwner)
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun showContentInDeveloping(contentName: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(InDevelopingDialogContent(contentName))
        findNavController().navigate(dir)
    }

    private fun showSomethingWentWrongDialog(target: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(SomethingWentWrong(target))
        findNavController().navigate(dir)
    }

}