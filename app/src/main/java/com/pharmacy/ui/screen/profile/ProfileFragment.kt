package com.pharmacy.ui.screen.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.databinding.FragmentProfileBinding
import com.pharmacy.ui.dialog.address_picker.AddressPickerDialog
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.profile.content.ProfileContent
import com.pharmacy.ui.screen.profile.model.mvi.ProfileSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class ProfileFragment : AppFragment(R.layout.fragment_profile) {

    private val viewModel by stateViewModel<ProfileViewModel>()
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddressPickerDialog.setResultListener(listener = viewModel::setAddress)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, state = {}, sideEffect = ::handleSideEffect)
        viewBinding.content.setContent { ProfileContent(viewModel) }
    }

    private fun handleSideEffect(effect: ProfileSideEffect) = when (effect) {
        is ProfileSideEffect.NavigateToAdministration -> navigateToAdministration()
        is ProfileSideEffect.SelectAddress -> showAddressSelection()
        is ProfileSideEffect.NavigateToOrders -> navigateToOrders()
        is ProfileSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is ProfileSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
    }

    private fun navigateToAdministration() {
        val dir = ProfileFragmentDirections.actionProfileFragmentToAdminMenuFragment()
        findNavController().navigate(dir)
    }

    private fun navigateToOrders() {
        val dir = ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
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

    private fun showAddressSelection() {
        findNavController().navigate(R.id.addressPickerDialog)
    }

}