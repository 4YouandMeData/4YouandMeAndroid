package com.foryouandme.ui.compose

import androidx.compose.ui.graphics.Color

fun Int?.toColor(): Color = this?.let { Color(it) } ?: Color.Transparent