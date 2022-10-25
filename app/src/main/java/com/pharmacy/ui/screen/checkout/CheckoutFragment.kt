package com.pharmacy.ui.screen.checkout

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.databinding.FragmentCheckoutBinding
import com.pharmacy.ui.dialog.address_picker.AddressPickerDialog
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.checkout.content.CheckoutScreenContent
import com.pharmacy.ui.screen.checkout.model.mvi.CheckoutSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class CheckoutFragment : AppFragment(R.layout.fragment_checkout) {

    private val viewModel by stateViewModel<CheckoutViewModel>()
    private val viewBinding by viewBinding(FragmentCheckoutBinding::bind)
    private val args by navArgs<CheckoutFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setProducts(args.products.toList())
        AddressPickerDialog.setResultListener(listener = viewModel::setAddress)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, null, sideEffect = ::handleSideEffect)
        viewBinding.content.setContent { CheckoutScreenContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: CheckoutSideEffect): Unit = when (effect) {
        is CheckoutSideEffect.AddressSelection -> showAddressSelection()
        is CheckoutSideEffect.NavigateBack -> navigateBack()
        is CheckoutSideEffect.NavigateToLogin -> navigateToLogin()
        is CheckoutSideEffect.NavigateNextAfterCheckout -> navigateNextAfterCheckout()
        is CheckoutSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is CheckoutSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.profile_graph)
    }

    private fun showAddressSelection() {
        findNavController().navigate(R.id.addressPickerDialog)
    }

    private fun navigateNextAfterCheckout() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.app_graph, true)
            .build()
        findNavController().navigate(R.id.showcase_graph, null, options)
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