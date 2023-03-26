package com.pharmacy.ui.screen.admin_reports_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminReportsMenuBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.admin_reports_menu.content.AdminReportsMenuContent
import com.pharmacy.ui.screen.admin_reports_menu.model.mvi.AdminReportsMenuSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AdminReportsMenuFragment : Fragment(R.layout.fragment_admin_reports_menu) {

    private val viewModel by stateViewModel<AdminReportsMenuViewModel>()
    private val viewBinding by viewBinding(FragmentAdminReportsMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, null, ::handleSideEffect)
        viewBinding.content.setContent { AdminReportsMenuContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: AdminReportsMenuSideEffect) = when (effect) {
        is AdminReportsMenuSideEffect.NavigateBack -> navigateBack()
        is AdminReportsMenuSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is AdminReportsMenuSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
        is AdminReportsMenuSideEffect.NavigateCategoriesReport -> navigateToCategoriesReport()
        is AdminReportsMenuSideEffect.NavigateProductsReport -> navigateToProductsReport()
        is AdminReportsMenuSideEffect.NavigateUsersReport -> navigateToUsersReport()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun showContentInDeveloping(contentName: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(InDevelopingDialogContent(contentName))
        findNavController().navigate(dir)
    }

    private fun showSomethingWentWrong(target: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(SomethingWentWrong(target))
        findNavController().navigate(dir)
    }

    private fun navigateToUsersReport() {
        val dir = AdminReportsMenuFragmentDirections
            .actionAdminReportsMenuFragmentToAdminReportsUsersFragment()
        findNavController().navigate(dir)
    }

    private fun navigateToCategoriesReport() {
        val dir = AdminReportsMenuFragmentDirections
            .actionAdminReportsMenuFragmentToAdminReportsCategoriesFragment()
        findNavController().navigate(dir)
    }

    private fun navigateToProductsReport() {
        val dir = AdminReportsMenuFragmentDirections
            .actionAdminReportsMenuFragmentToAdminReportsProductsFragment()
        findNavController().navigate(dir)
    }

}