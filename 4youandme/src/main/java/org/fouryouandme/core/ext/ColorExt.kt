package org.fouryouandme.core.ext

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.StateSet
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

@ColorInt
fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {

    val alpha = (255 * factor).roundToInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)

}

fun selectedUnselectedColor(selected: Int, unselected: Int): ColorStateList =
    ColorStateList(
        arrayOf(
            arrayOf(android.R.attr.state_checked).toIntArray(),
            StateSet.WILD_CARD
        ),
        arrayOf(selected, unselected).toIntArray()
    )