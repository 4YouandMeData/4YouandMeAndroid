package com.fouryouandme.core.ext

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager

fun Activity.transparentStatusBar() {

    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        statusBarColor = Color.TRANSPARENT
    }

}