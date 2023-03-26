package com.pharmacy.ui.screen.showcase

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.databinding.FragmentShowcaseBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.showcase.content.ShowcaseScreenContent
import com.pharmacy.ui.screen.showcase.model.mvi.ShowcaseSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class ShowcaseFragment : AppFragment(R.layout.fragment_showcase) {

    private val viewModel by stateViewModel<ShowcaseViewModel>()
    private val viewBinding by viewBinding(FragmentShowcaseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { ShowcaseScreenContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: ShowcaseSideEffect) = when (effect) {
        is ShowcaseSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is ShowcaseSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
        is ShowcaseSideEffect.OpenProductDetails -> navigateToProductDetails(effect.productId)
    }

    private fun showContentInDeveloping(contentName: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(InDevelopingDialogContent(contentName))
        findNavController().navigate(dir)
    }

    private fun showSomethingWentWrong(target: String? = null) {
        val dir = AppGraphDirections.actionComposableDialog(SomethingWentWrong(target))
        findNavController().navigate(dir)
    }

    private fun navigateToProductDetails(productId: Int) {
        val dir = "https://pharmacy/product/$productId".toUri() // TODO: DeepLink
        findNavController().navigate(dir)
    }

}