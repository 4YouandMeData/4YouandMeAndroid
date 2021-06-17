package com.foryouandme.core.ext

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Brush.Companion.color(color: Color): Brush =
    linearGradient(listOf(color, color))

fun Brush.Companion.transparent(): Brush =
    linearGradient(listOf(Color.Transparent, Color.Transparent))