package com.foryouandme.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ForYouAndMeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        typography = ForYouAndMeTheme.typography
    )
}

object ForYouAndMeTheme {

    val typography = ForYouAndMeTypography

}



