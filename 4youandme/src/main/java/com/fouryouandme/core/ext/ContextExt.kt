package com.fouryouandme.core.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.fouryouandme.core.arch.android.AppInjector
import com.fouryouandme.core.arch.deps.ImageConfiguration
import com.fouryouandme.core.arch.deps.Injector
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.navigation.Navigator
import es.dmoral.toasty.Toasty

/* --- resources --- */

@ColorInt
fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

/* --- injector --- */

val Context.injector: Injector
    get() = (applicationContext as AppInjector).injector

val Context.navigator: Navigator
    get() = injector.navigator

val Context.imageConfiguration: ImageConfiguration
    get() = injector.imageConfiguration

/* --- toast --- */

fun Context.errorToast(message: String): Unit =
    Toasty.error(this, message, Toast.LENGTH_LONG).show()

fun Context.errorToast(error: FourYouAndMeError): Unit =
    Toasty.error(this, error.message(this), Toast.LENGTH_LONG).show()

fun Context.infoToast(message: String): Unit =
    Toasty.info(this, message, Toast.LENGTH_LONG).show()