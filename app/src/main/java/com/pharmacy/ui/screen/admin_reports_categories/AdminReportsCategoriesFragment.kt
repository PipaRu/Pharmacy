package com.pharmacy.ui.screen.admin_reports_categories

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminReportsCategoriesBinding
import com.pharmacy.ui.screen.admin_reports_categories.content.AdminReportsCategoriesContent
import com.pharmacy.ui.screen.admin_reports_categories.model.mvi.AdminReportsCategoriesSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AdminReportsCategoriesFragment : Fragment(R.layout.fragment_admin_reports_categories) {

    private val viewModel by stateViewModel<AdminReportsCategoriesViewModel>()
    private val viewBinding by viewBinding(FragmentAdminReportsCategoriesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect,
        )
        viewBinding.content.setContent { AdminReportsCategoriesContent(viewModel) }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { viewModel.back() }
    }

    private fun handleSideEffect(effect: AdminReportsCategoriesSideEffect) = when (effect) {
        AdminReportsCategoriesSideEffect.NavigateBack -> navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

}