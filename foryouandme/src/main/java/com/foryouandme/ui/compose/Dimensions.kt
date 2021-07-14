package com.foryouandme.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class Dimensions(
    val bottomBarTextSize: TextUnit
)

val smallDimensions =
    Dimensions(
        bottomBarTextSize = 10.sp
    )

val w360Dimensions =
    Dimensions(
        bottomBarTextSize = 12.sp
    )

@Composable
fun ProvideDimens(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

internal val LocalAppDimens = staticCompositionLocalOf { smallDimensions }