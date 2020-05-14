package org.fouryouandme.core.entity.configuration.button

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import kotlin.math.roundToInt

fun button(color: Int): StateListDrawable {

    val normalDrawable = createButtonDrawable(color)

    val pressedDrawable = createButtonDrawable(adjustAlpha(color, 0.8f))

    val stateDrawable = StateListDrawable()
    stateDrawable.addState(arrayOf(android.R.attr.state_pressed).toIntArray(), pressedDrawable)
    stateDrawable.addState(StateSet.WILD_CARD, normalDrawable)
    return stateDrawable

}

fun button(resources: Resources, @DrawableRes drawable: Int): StateListDrawable {

    val normalDrawable = loadDrawable(resources, drawable, 255)

    val pressedDrawable = loadDrawable(resources, drawable, 204)

    val disabledDrawable = loadDrawable(resources, drawable, 130)

    val stateDrawable = StateListDrawable()
    stateDrawable.addState(arrayOf(android.R.attr.state_pressed).toIntArray(), pressedDrawable)
    stateDrawable.addState(arrayOf(-android.R.attr.state_enabled).toIntArray(), disabledDrawable)
    stateDrawable.addState(StateSet.WILD_CARD, normalDrawable)
    return stateDrawable

}

private fun createButtonDrawable(color: Int): GradientDrawable {

    val drawable = GradientDrawable()
    drawable.setColor(color)
    drawable.cornerRadii = arrayOf(60f, 60f, 60f, 60f, 60f, 60f, 60f, 60f).toFloatArray()
    return drawable

}

@ColorInt
private fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {

    val alpha = (Color.alpha(color) * factor).roundToInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)

}

private fun loadDrawable(resources: Resources, id: Int, alpha: Int): Drawable {

    val drawable =
        resources.getDrawable(id, null)

    val updatedDrawable =
        drawable.constantState
            ?.newDrawable()
            ?.mutate()

    updatedDrawable?.alpha = alpha

    return updatedDrawable ?: drawable
}