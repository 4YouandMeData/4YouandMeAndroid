package com.foryouandme.ui.compose.statusbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberAndroidSystemUiController

@Composable
fun StatusBar(
    color: Color,
    content: @Composable () -> Unit
) {
    ProvideWindowInsets {
        val systemUiController = rememberAndroidSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color,
                darkIcons = isDark(color).not()
            )
        }
        content()
    }
}

private fun isDark(color: Color): Boolean =
    ((color.red * 0.299) + (color.green * 0.587) + (color.blue * 0.114)) < 186