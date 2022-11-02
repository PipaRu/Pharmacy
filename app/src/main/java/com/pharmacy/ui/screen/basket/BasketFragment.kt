package com.pharmacy.ui.screen.basket

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.common.ui.fragment.result.launcher.registerForFragmentResult
import com.pharmacy.databinding.FragmentBasketBinding
import com.pharmacy.ui.dialog.authorization_required.AuthorizationRequiredDialog
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.screen.basket.content.BasketScreenContent
import com.pharmacy.ui.screen.basket.model.mvi.BasketSideEffect
import com.pharmacy.ui.screen.checkout.CheckoutFragmentArgs
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class BasketFragment : AppFragment(R.layout.fragment_basket) {

    private val authorizationRequiredDialog = registerForFragmentResult(
        fragmentResult = AuthorizationRequiredDialog,
        launch = { showAuthorizationRequiredDialog() },
        listener = { navigateToProfile() }
    )

    private val viewModel by stateViewModel<BasketViewModel>()
    private val viewBinding by viewBinding(FragmentBasketBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { BasketScreenContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: BasketSideEffect) = when (effect) {
        is BasketSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is BasketSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
        is BasketSideEffect.ShowAuthorizationRequired -> authorizationRequiredDialog.launch()
        is BasketSideEffect.ShowCheckout -> showCheckout(effect.products)
    }

    private fun showContentInDeveloping(contentName: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(InDevelopingDialogContent(contentName))
        findNavController().navigate(dir)
    }

    private fun showSomethingWentWrong(target: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(SomethingWentWrong(target))
        findNavController().navigate(dir)
    }

    private fun showAuthorizationRequiredDialog() {
        findNavController().navigate(R.id.authorizationRequiredDialog)
    }

    private fun showCheckout(products: List<BasketBunchItem>) {
        val args = CheckoutFragmentArgs(products.toTypedArray()).toBundle()
        findNavController().navigate(R.id.checkoutFragment, args)
    }

    private fun navigateToProfile() {
        findNavController().navigate(R.id.profile_graph)
    }

}