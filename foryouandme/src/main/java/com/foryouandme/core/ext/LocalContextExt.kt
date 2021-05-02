package com.foryouandme.core.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.foryouandme.core.arch.deps.ImageConfiguration

val imageConfiguration: ImageConfiguration
    @Composable
    @ReadOnlyComposable
    get() = LocalContext.current.imageConfiguration