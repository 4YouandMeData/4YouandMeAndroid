package org.fouryouandme.core.ext

import android.app.Application
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import arrow.fx.ForIO
import arrow.fx.IO
import org.fouryouandme.core.arch.android.AppInjector
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
