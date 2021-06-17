package com.foryouandme.ui.compose.button

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.foryouandme.core.ext.launchSafe

@ExperimentalComposeUiApi
@Composable
fun ImageButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painter,
        contentDescription = null,
        modifier =
        modifier
            .scale(scale.value)
            .pointerInteropFilter {

                if (it.action == 0) {
                    if (scale.isRunning.not()) {
                        onClick()

                        coroutineScope.launchSafe {
                            scale.animateTo(
                                1.4f,
                                tween(durationMillis = 400)
                            )
                            scale.animateTo(
                                1f,
                                tween(durationMillis = 400)
                            )
                        }
                    }
                }

                true

            }

    )
}