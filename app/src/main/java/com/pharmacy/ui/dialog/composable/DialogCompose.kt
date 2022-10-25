package com.pharmacy.ui.dialog.composable

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.AppDialogFragment
import com.pharmacy.common.ui.fragment.result.compose.LocalFragmentResultOwnerProvider
import com.pharmacy.databinding.DialogComposableBinding

class DialogCompose : AppDialogFragment(R.layout.dialog_composable) {

    private val viewBinding by viewBinding(DialogComposableBinding::bind)
    private val args by navArgs<DialogComposeArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.content.setContent {
            CompositionLocalProvider(LocalFragmentResultOwnerProvider provides this) {
                args.builder.Build(
                    onClose = { dismiss() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val attrs = dialog?.window?.attributes?.apply {
            this.width = ViewGroup.LayoutParams.MATCH_PARENT
            this.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        dialog?.window?.attributes = attrs
    }

}