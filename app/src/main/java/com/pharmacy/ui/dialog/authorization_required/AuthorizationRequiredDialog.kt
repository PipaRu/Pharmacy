package com.pharmacy.ui.dialog.authorization_required

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.pharmacy.common.ui.fragment.AppBottomSheetDialogFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.ui.dialog.authorization_required.content.AuthorizationRequiredContent

class AuthorizationRequiredDialog : AppBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AuthorizationRequiredContent(
                    onLogin = { OnLogin.trigger() }
                )
            }
        }
    }

    companion object OnLogin : FragmentResultHost.ValueUnit(
        defaultKey = "authorization_required_dialog_request_key"
    )

}