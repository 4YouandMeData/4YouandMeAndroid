package org.fouryouandme.core.ext

import android.app.Application
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import arrow.fx.ForIO
import arrow.fx.IO
import org.fouryouandme.core.arch.android.AppInjector
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Injector
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.runtime
import org.fouryouandme.core.arch.navigation.Navigator


/* --- resources --- */

@ColorInt
fun Fragment.color(@ColorRes res: Int): Int = ContextCompat.getColor(requireContext(), res)

/* --- injector --- */

val Fragment.injector: Injector
    get() = (requireActivity().applicationContext as AppInjector).injector

val Fragment.IORuntime: Runtime<ForIO>
    get() = IO.runtime(injector, requireActivity().applicationContext as Application)

val Fragment.navigator: Navigator
    get() = injector.navigator

val Fragment.imageConfiguration: ImageConfiguration
    get() = injector.imageConfiguration

/* --- status bar --- */

fun Fragment.setStatusBar(backgroundColor: Int): Unit {

    val red = Color.red(backgroundColor)
    val green = Color.green(backgroundColor)
    val blue = Color.blue(backgroundColor)

    val isDark = ((red * 0.299) + (green * 0.587) + (blue * 0.114)) < 186

    if (isDark) { // set with icons
        var flags = requireActivity().window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        requireActivity().window.decorView.systemUiVisibility = flags
    } else { // set black icons
        var flags = requireActivity().window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        requireActivity().window.decorView.systemUiVisibility = flags
    }

}
