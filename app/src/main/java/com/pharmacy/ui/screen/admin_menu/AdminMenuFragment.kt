package com.pharmacy.ui.screen.admin_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminMenuBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.admin_menu.content.AdminMenuScreenContent
import com.pharmacy.ui.screen.admin_menu.model.mvi.AdminMenuSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AdminMenuFragment : Fragment(R.layout.fragment_admin_menu) {

    private val viewModel by stateViewModel<AdminMenuViewModel>()
    private val viewBinding by viewBinding(FragmentAdminMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { AdminMenuScreenContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: AdminMenuSideEffect) = when (effect) {
        is AdminMenuSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is AdminMenuSideEffect.ShowSomethingWentWrong -> showSomethingWentWrongDialog(effect.target)
        is AdminMenuSideEffect.NavigateBack -> navigateBack()
        is AdminMenuSideEffect.NavigateToProducts -> navigateToProducts()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun navigateToProducts() {
        val dir = AdminMenuFragmentDirections.actionAdminMenuFragmentToAdminProductsFragment()
        findNavController().navigate(dir)
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