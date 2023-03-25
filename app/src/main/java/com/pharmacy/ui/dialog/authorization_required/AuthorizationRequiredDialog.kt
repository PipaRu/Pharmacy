package com.pharmacy.ui.dialog.authorization_required

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.pharmacy.common.ui.fragment.AppBottomSheetDialogFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.ui.dialog.authorization_required.content.AuthorizationRequiredScreenContent
import com.pharmacy.ui.dialog.authorization_required.mvi.AuthorizationRequiredSideEffect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AuthorizationRequiredDialog : AppBottomSheetDialogFragment() {

    companion object OnLogin : FragmentResultHost.ValueUnit(
        defaultKey = "authorization_required_dialog_request_key"
    )

    private val viewModel by stateViewModel<AuthorizationRequiredDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AuthorizationRequiredScreenContent(viewModel = viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(lifecycleOwner = viewLifecycleOwner, state = null, sideEffect = ::handleSideEffect)
    }

    private fun handleSideEffect(effect: AuthorizationRequiredSideEffect) = when (effect) {
        is AuthorizationRequiredSideEffect.OnLoginSuccess -> {
            OnLogin.trigger()
            dismiss()
        }
    }

}