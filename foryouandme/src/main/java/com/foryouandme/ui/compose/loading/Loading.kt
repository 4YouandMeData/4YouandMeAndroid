package com.foryouandme.ui.compose.loading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.ext.noIndicationClickable
import com.foryouandme.entity.configuration.Configuration

@Composable
fun Loading(
    configuration: Configuration?,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    Loading(
        backgroundColor =
        (configuration?.theme?.secondaryColor?.value ?: Color.White).copy(alpha = 0.49f),
        loadingColor = configuration?.theme?.primaryColorStart?.value,
        isVisible = isVisible,
        modifier = modifier
    )
}

@Composable
fun Loading(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    loadingColor: Color? = null,
    isVisible: Boolean = true
) {
    Loading(
        backgroundColor = backgroundColor,
        loadingImage = R.drawable.loading,
        loadingColor = loadingColor,
        isVisible = isVisible,
        modifier = modifier
    )
}

@Composable
fun Loading(
    backgroundColor: Color,
    loadingImage: Int,
    loadingColor: Color?,
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
            modifier = modifier
                .background(backgroundColor)
                .noIndicationClickable { },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = loadingImage),
                contentDescription = null,
                colorFilter = if (loadingColor != null) ColorFilter.tint(loadingColor) else null,
                modifier =
                Modifier
                    .size(40.dp)
                    .rotate(rotation)
            )
        }

}