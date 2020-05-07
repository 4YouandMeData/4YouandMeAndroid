package org.fouryouandme.core.entity.theme

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

data class HEXColor(val hex: String)

data class HEXGradient(val primaryHex: String, val secondaryHex: String) {

    fun drawable(): GradientDrawable {

        val gd =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.parseColor(primaryHex),
                    Color.parseColor(secondaryHex)
                )
            )
        gd.cornerRadius = 0f
        return gd
    }
}

data class Theme(
    val primaryStart: HEXColor,
    val primaryEnd: HEXColor,
    val primaryGradient: HEXGradient
)