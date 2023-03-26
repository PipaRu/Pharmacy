package com.pharmacy.ui.screen.product_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.databinding.FragmentAdminProductDetailsBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.screen.product_details.content.ProductDetailsContent
import com.pharmacy.ui.screen.product_details.model.mvi.ProductDetailsSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val viewModel by stateViewModel<ProductDetailsViewModel>()
    private val viewBinding by viewBinding(FragmentAdminProductDetailsBinding::bind)
    private val args by navArgs<ProductDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setProduct(args.productId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { ProductDetailsContent(viewModel = viewModel) }
    }

    private fun handleSideEffect(effect: ProductDetailsSideEffect) = when (effect) {
        is ProductDetailsSideEffect.ShowContentInDeveloping -> {
            showContentInDeveloping(effect.contentName)
        }
        is ProductDetailsSideEffect.ShowSomethingWentWrong -> {
            showSomethingWentWrongDialog(effect.target)
        }
        is ProductDetailsSideEffect.NavigateBack -> {
            navigateBack()
        }
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