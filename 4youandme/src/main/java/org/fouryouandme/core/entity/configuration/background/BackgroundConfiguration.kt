package org.fouryouandme.core.entity.configuration.background

import android.graphics.drawable.GradientDrawable
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.dpToPx

fun roundBackground(
    color: Int,
    topStartDp: Int,
    topEndDp: Int,
    bottomEndDp: Int,
    bottomStartDp: Int,
): GradientDrawable {

    val topStartPx = topStartDp.dpToPx().toFloat()
    val topEndPx = topEndDp.dpToPx().toFloat()
    val bottomEndPx = bottomEndDp.dpToPx().toFloat()
    val bottomStartPx = bottomStartDp.dpToPx().toFloat()

    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.cornerRadii =
        arrayOf(
            topStartPx,
            topStartPx,
            topEndPx,
            topEndPx,
            bottomEndPx,
            bottomEndPx,
            bottomStartPx,
            bottomStartPx
        ).toFloatArray()
    return drawable

}

fun roundBackground(color: Int, radiusDp: Int = 20): GradientDrawable =
    roundBackground(color, radiusDp, radiusDp, radiusDp, radiusDp)

fun roundTopBackground(color: Int, radiusDp: Int = 30): GradientDrawable =
    roundBackground(color, radiusDp, radiusDp, 0, 0)

fun shadow(color: Int): GradientDrawable =
    HEXGradient.from(
        HEXColor.transparent(),
        HEXColor.parse(color)
    ).drawable(0.3f)

