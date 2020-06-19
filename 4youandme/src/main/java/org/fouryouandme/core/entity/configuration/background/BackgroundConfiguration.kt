package org.fouryouandme.core.entity.configuration.background

import android.graphics.drawable.GradientDrawable

fun roundBackground(color: Int): GradientDrawable {

    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.cornerRadii = arrayOf(60f, 60f, 60f, 60f, 60f, 60f, 60f, 60f).toFloatArray()
    return drawable

}

