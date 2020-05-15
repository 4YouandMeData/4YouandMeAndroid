package org.fouryouandme.core.ext

import android.content.Context
import android.util.TypedValue

fun Float.spToPx(context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    ).toInt()