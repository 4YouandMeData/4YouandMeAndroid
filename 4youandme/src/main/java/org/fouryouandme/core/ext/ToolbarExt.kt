package org.fouryouandme.core.ext

import androidx.appcompat.widget.Toolbar
import org.fouryouandme.core.arch.deps.ImageConfiguration

fun Toolbar.showBackButton(
    imageConfiguration: ImageConfiguration,
    back: () -> Unit
) {
    setNavigationIcon(imageConfiguration.back())
    setNavigationOnClickListener { back() }
}

fun Toolbar.showCloseButton(
    imageConfiguration: ImageConfiguration,
    back: () -> Unit
) {
    setNavigationIcon(imageConfiguration.close())
    setNavigationOnClickListener { back() }
}