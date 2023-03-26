package com.pharmacy.ui.screen.admin_reports_products

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminReportsProductsBinding
import com.pharmacy.ui.screen.admin_reports_products.content.AdminReportsProductsContent
import com.pharmacy.ui.screen.admin_reports_products.model.mvi.AdminReportsProductsSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AdminReportsProductsFragment : Fragment(R.layout.fragment_admin_reports_products) {

    private val viewModel by stateViewModel<AdminReportsProductsViewModel>()
    private val viewBinding by viewBinding(FragmentAdminReportsProductsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect,
        )
        viewBinding.content.setContent { AdminReportsProductsContent(viewModel) }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { viewModel.back() }
    }

    private fun handleSideEffect(effect: AdminReportsProductsSideEffect) = when (effect) {
        AdminReportsProductsSideEffect.NavigateBack -> navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

}