package com.foryouandme.ui.compose.loading

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.noIndicationClickable
import com.foryouandme.entity.configuration.Configuration
import com.google.accompanist.coil.CoilImage

@Composable
fun Loading(
    configuration: Configuration?,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    Loading(
        backgroundColor = configuration?.theme?.secondaryColor?.value ?: Color.White,
        isVisible = isVisible,
        modifier = modifier
    )
}

@Composable
fun Loading(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    Loading(
        backgroundColor = backgroundColor.copy(alpha = 0.49f),
        loadingImage = LocalContext.current.imageConfiguration.loading(),
        isVisible = isVisible,
        modifier = modifier
    )
}

@Composable
fun Loading(
    backgroundColor: Color,
    loadingImage: Int,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    if (isVisible)
        Box(
            modifier = modifier.background(backgroundColor).noIndicationClickable {  },
            contentAlignment = Alignment.Center
        ) {
            CoilImage(
                data = loadingImage,
                contentDescription = null,
                modifier =
                Modifier
                    .size(40.dp)
                    .rotate(rotation)
            )
        }

}