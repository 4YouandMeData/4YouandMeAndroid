package com.foryouandme.ui.compose.statusbar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberAndroidSystemUiController

@Composable
fun TransparentStatusBar(
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    ProvideWindowInsets {
        val systemUiController = rememberAndroidSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                Color.Transparent,
                darkIcons = isDark(backgroundColor).not()
            )
        }
        content()
    }
}

private fun isDark(color: Color): Boolean =
    ((color.red * 0.299) + (color.green * 0.587) + (color.blue * 0.114)) < 186

@Composable
fun StatusBarSpacer() {
    Spacer(
        modifier =
        Modifier
            .statusBarsHeight() // Match the height of the status bar
            .fillMaxWidth()
    )
}