package com.foryouandme.core.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.foryouandme.core.arch.android.AppInjector
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.Injector
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.error.getMessage
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

val Context.videoConfiguration: VideoConfiguration
    get() = injector.videoConfiguration

/* --- toast --- */

fun Context.errorToast(message: String) {
    Toasty.error(this, message, Toast.LENGTH_LONG).show()
}

fun Context.errorToast(error: ForYouAndMeError) {
    Toasty.error(this, error.message(this), Toast.LENGTH_LONG).show()
}

fun Context.errorToast(error: ForYouAndMeException, configuration: Configuration? = null) {
    Toasty.error(
        this,
        error.getMessage(configuration).getText(this),
        Toast.LENGTH_LONG
    ).show()
}

fun Context.infoToast(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG).show()
}

/* --- window --- */

fun Context.findWindow(): Window? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}