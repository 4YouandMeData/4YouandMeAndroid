package org.fouryouandme.core.ext

import android.app.Application
import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
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
fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

/* --- injector --- */

val Context.injector: Injector
    get() = (applicationContext as AppInjector).injector

val Context.IORuntime: Runtime<ForIO>
    get() = IO.runtime(injector, applicationContext as Application)

val Context.navigator: Navigator
    get() = injector.navigator

val Context.imageConfiguration: ImageConfiguration
    get() = injector.imageConfiguration