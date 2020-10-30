package com.foryouandme.core.entity.configuration

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import arrow.core.getOrElse
import arrow.core.toOption
import com.foryouandme.core.ext.adjustAlpha

data class HEXColor(val hex: String) {

    @ColorInt
    fun color(): Int = Color.parseColor(hex)

    companion object {

        fun transparent(): HEXColor = HEXColor("#00FFFFFF")

        fun parse(color: Int): HEXColor =
            HEXColor(String.format("#%08X", -0x1 and color))

    }
}

fun String.toHEXColor(): HEXColor =
    HEXColor(this)

data class HEXGradient(val primaryHex: String, val secondaryHex: String) {

    fun drawable(alphaOverride: Float? = null): GradientDrawable {

        val start =
            alphaOverride.toOption()
                .map { adjustAlpha(Color.parseColor(primaryHex), it) }
                .getOrElse { Color.parseColor(primaryHex) }
        val end =
            alphaOverride.toOption()
                .map { adjustAlpha(Color.parseColor(secondaryHex), it) }
                .getOrElse { Color.parseColor(secondaryHex) }

        val gd =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    start,
                    end
                )
            )
        gd.cornerRadius = 0f
        return gd
    }

    companion object {

        fun from(start: HEXColor, end: HEXColor): HEXGradient =
            HEXGradient(
                start.hex,
                end.hex
            )

    }
}

data class Theme(
    val primaryColorStart: HEXColor,
    val primaryColorEnd: HEXColor,
    val secondaryColor: HEXColor,
    val tertiaryColorStart: HEXColor,
    val tertiaryColorEnd: HEXColor,
    val primaryTextColor: HEXColor,
    val secondaryTextColor: HEXColor,
    val tertiaryTextColor: HEXColor,
    val fourthTextColor: HEXColor,
    val primaryMenuColor: HEXColor,
    val secondaryMenuColor: HEXColor,
    val activeColor: HEXColor,
    val deactiveColor: HEXColor,
    val fourthColor: HEXColor
)