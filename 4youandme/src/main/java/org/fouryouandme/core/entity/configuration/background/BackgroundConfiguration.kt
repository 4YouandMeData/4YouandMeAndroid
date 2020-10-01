package org.fouryouandme.core.entity.configuration.background

import android.graphics.drawable.GradientDrawable
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.dpToPx

fun roundBackground(color: Int, radiusDp: Int = 20): GradientDrawable {

    val radiusPx = radiusDp.dpToPx().toFloat()

    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.cornerRadii =
        arrayOf(
            radiusPx,
            radiusPx,
            radiusPx,
            radiusPx,
            radiusPx,
            radiusPx,
            radiusPx,
            radiusPx
        ).toFloatArray()
    return drawable

}

fun roundTopBackground(color: Int, radiusDp: Int = 30): GradientDrawable {

    val radiusPx = radiusDp.dpToPx().toFloat()

    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.cornerRadii = arrayOf(
        radiusPx,
        radiusPx,
        radiusPx,
        radiusPx,
        0f,
        0f,
        0f,
        0f
    ).toFloatArray()
    return drawable
}

fun shadow(color: Int): GradientDrawable =
    HEXGradient.from(
        HEXColor.transparent(),
        HEXColor.parse(color)
    ).drawable(0.3f)

