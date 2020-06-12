package org.fouryouandme.core.ext

import android.graphics.Color
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