package com.foryouandme.core.ext

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.foryouandme.core.arch.error.ForYouAndMeError


/* --- resources --- */

@ColorInt
fun Fragment.color(@ColorRes res: Int): Int = ContextCompat.getColor(requireContext(), res)

/* --- status bar --- */

fun Fragment.setStatusBar(backgroundColor: Int): Unit {

    requireActivity().window.statusBarColor = backgroundColor

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

/* --- parent --- */

fun Fragment.sectionParent(): Fragment = requireParentFragment().requireParentFragment()

inline fun <reified T : Fragment> Fragment.find(): T {

    var parent = requireParentFragment()

    while (parent !is T) {
        parent = parent.requireParentFragment()
    }

    return parent

}

/* --- toast --- */

fun Fragment.errorToast(message: String): Unit = requireContext().errorToast(message)

fun Fragment.errorToast(error: ForYouAndMeError): Unit = requireContext().errorToast(error)

fun Fragment.infoToast(message: String): Unit = requireContext().infoToast(message)

/* --- keyboard --- */

suspend fun Fragment.hideKeyboardSuspend(): Unit =
    requireActivity().hideKeyboardSuspend()

fun Fragment.hideKeyboard(): Unit =
    requireActivity().hideKeyboard()

suspend fun Fragment.showKeyboard(): Unit =
    requireActivity().showKeyboard()