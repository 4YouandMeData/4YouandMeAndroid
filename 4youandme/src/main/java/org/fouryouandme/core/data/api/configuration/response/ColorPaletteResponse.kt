package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.Theme
import org.fouryouandme.core.entity.configuration.toHEXColor

data class ColorPaletteResponse(

    @Json(name="primary_color_end") val primaryColorEnd: String? = null,
    @Json(name="primary_color_start") val primaryColorStart: String? = null,
    @Json(name="primary_menu_color") val primaryMenuColor: String? = null,
    @Json(name="tertiary_color_start") val tertiaryColorStart: String? = null,
    @Json(name="primary_text_color") val primaryTextColor: String? = null,
    @Json(name="secondary_menu_color") val secondaryMenuColor: String? = null,
    @Json(name="active_color") val activeColor: String? = null,
    @Json(name="deactive_color") val deactiveColor: String? = null,
    @Json(name="secondary_color") val secondaryColor: String? = null,
    @Json(name="tertiary_color_end") val tertiaryColorEnd: String? = null,
    @Json(name="fourth_text_color") val fourthTextColor: String? = null,
    @Json(name="tertiary_text_color") val tertiaryTextColor: String? = null,
    @Json(name="secondary_text_color") val secondaryTextColor: String? = null,
    @Json(name="fourth_color") val fourthColor: String? = null

) {

    fun toTheme(): Option<Theme> =
        Option.fx {

            Theme(
                primaryColorStart.toOption().bind().toHEXColor(),
                primaryColorEnd.toOption().bind().toHEXColor(),
                secondaryColor.toOption().bind().toHEXColor(),
                tertiaryColorStart.toOption().bind().toHEXColor(),
                tertiaryColorEnd.toOption().bind().toHEXColor(),
                primaryTextColor.toOption().bind().toHEXColor(),
                secondaryTextColor.toOption().bind().toHEXColor(),
                tertiaryTextColor.toOption().bind().toHEXColor(),
                fourthTextColor.toOption().bind().toHEXColor(),
                primaryMenuColor.toOption().bind().toHEXColor(),
                secondaryMenuColor.toOption().bind().toHEXColor(),
                activeColor.toOption().bind().toHEXColor(),
                deactiveColor.toOption().bind().toHEXColor(),
                fourthColor.toOption().bind().toHEXColor()
            )

        }
}