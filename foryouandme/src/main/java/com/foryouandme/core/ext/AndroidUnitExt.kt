package com.foryouandme.core.ext

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

fun Float.spToPx(context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    ).toInt()

fun Int.dpToPx(): Int =
    (this * (
            Resources.getSystem()
                .displayMetrics
                .densityDpi
                .toFloat()
                    / DisplayMetrics.DENSITY_DEFAULT))
        .roundToInt()

fun Float.dpToPx(): Int =
    (this * (
            Resources.getSystem()
                .displayMetrics
                .densityDpi
                .toFloat()
                    / DisplayMetrics.DENSITY_DEFAULT))
        .roundToInt()

