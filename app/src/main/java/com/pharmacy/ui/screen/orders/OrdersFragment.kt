package com.pharmacy.ui.screen.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pharmacy.AppGraphDirections
import com.pharmacy.R
import com.pharmacy.common.extensions.showWithLifecycle
import com.pharmacy.databinding.FragmentOrdersBinding
import com.pharmacy.ui.dialog.in_developing.InDevelopingDialogContent
import com.pharmacy.ui.dialog.sww.SomethingWentWrong
import com.pharmacy.ui.model.OrderItem
import com.pharmacy.ui.screen.orders.content.OrdersScreenContent
import com.pharmacy.ui.screen.orders.model.mvi.OrdersSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val viewModel by stateViewModel<OrdersViewModel>()
    private val viewBinding by viewBinding(FragmentOrdersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
        viewBinding.content.setContent { OrdersScreenContent(viewModel) }
    }

    private fun handleSideEffect(effect: OrdersSideEffect) = when (effect) {
        is OrdersSideEffect.ShowContentInDeveloping -> showContentInDeveloping(effect.contentName)
        is OrdersSideEffect.ShowSomethingWentWrong -> showSomethingWentWrong(effect.target)
        is OrdersSideEffect.CancelOrderWarning -> showCancelOrderWarningDialog(effect.order)
        is OrdersSideEffect.NavigateBack -> navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun showCancelOrderWarningDialog(order: OrderItem) {
        val message = getString(R.string.content_orders_cancel_order_message, order.id.toString())
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.content_orders_cancel_order_action_positive) { _, _ ->
                viewModel.cancelOrder(order)
            }
            .setNegativeButton(R.string.content_orders_cancel_order_action_negative, null)
            .create()
            .showWithLifecycle(viewLifecycleOwner)
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