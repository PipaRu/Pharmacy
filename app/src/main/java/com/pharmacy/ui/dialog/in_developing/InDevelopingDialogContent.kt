package com.pharmacy.ui.dialog.in_developing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.pharmacy.R
import com.pharmacy.common.ui.fragment.result.FragmentResultDispatcher.Companion.trigger
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.common.ui.fragment.result.compose.getDispatcher
import com.pharmacy.ui.dialog.composable.ComposableDialogBuilder
import kotlinx.parcelize.Parcelize
import com.pharmacy.R.string.content_in_developing_action as action
import com.pharmacy.R.string.content_in_developing_message as message

@Parcelize
class InDevelopingDialogContent(
    private val sectionName: String? = null,
) : ComposableDialogBuilder {

    @Composable
    override fun Build(onClose: () -> Unit) {
        val okAction = OkAction.getDispatcher()
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sectionName != null) {
                        Text(
                            text = sectionName,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ImageInDeveloping(
                            modifier = Modifier.size(256.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(message),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp),
                        onClick = {
                            okAction.trigger()
                            onClose.invoke()
                        }
                    ) {
                        Text(text = stringResource(action))
                    }
                }
            }
        }
    }

    @Composable
    private fun ImageInDeveloping(
        modifier: Modifier = Modifier,
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.anim_lottie_in_developing)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = modifier,
            composition = composition,
            progress = { progress },
        )
    }

    object OkAction : FragmentResultHost.ValueUnit(
        defaultKey = "in_developing_dialog_content"
    )

}

