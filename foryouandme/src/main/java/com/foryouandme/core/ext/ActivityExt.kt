package com.foryouandme.core.ext

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

/* --- status bar --- */

fun Activity.transparentStatusBar() {

    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        statusBarColor = Color.TRANSPARENT
    }

}

/* --- keyboard --- */

fun Activity.hideKeyboard() {

    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null)
        view = View(this)

    imm.hideSoftInputFromWindow(view.windowToken, 0)

}

fun Activity.showKeyboard() {

    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

}