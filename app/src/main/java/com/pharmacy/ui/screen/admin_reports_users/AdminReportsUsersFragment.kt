package com.pharmacy.ui.screen.admin_reports_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminReportsUsersBinding
import com.pharmacy.ui.screen.admin_reports_users.content.AdminReportsUsersContent
import com.pharmacy.ui.screen.admin_reports_users.model.mvi.AdminReportsUsersSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AdminReportsUsersFragment : Fragment(R.layout.fragment_admin_reports_users) {

    private val viewModel by stateViewModel<AdminReportsUsersViewModel>()
    private val viewBinding by viewBinding(FragmentAdminReportsUsersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect,
        )
        viewBinding.content.setContent { AdminReportsUsersContent(viewModel) }
    }

    private fun handleSideEffect(effect: AdminReportsUsersSideEffect) = when (effect) {
        AdminReportsUsersSideEffect.NavigateBack -> navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

}