package com.foryouandme.ui.compose

import androidx.compose.ui.graphics.Brush
import com.foryouandme.entity.configuration.Theme

val Theme.verticalGradient: Brush
    get() = Brush.verticalGradient(listOf(
        primaryColorStart.value,
        primaryColorStart.value,
        primaryColorStart.value,
        primaryColorEnd.value
    ))