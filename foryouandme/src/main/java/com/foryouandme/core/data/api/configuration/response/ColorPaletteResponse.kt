package com.foryouandme.core.data.api.configuration.response

import arrow.core.Either
import com.foryouandme.entity.configuration.Theme
import com.foryouandme.entity.configuration.toHEXColor
import com.squareup.moshi.Json

data class ColorPaletteResponse(

    @Json(name = "primary_color_end") val primaryColorEnd: String? = null,
    @Json(name = "primary_color_start") val primaryColorStart: String? = null,
    @Json(name = "primary_menu_color") val primaryMenuColor: String? = null,
    @Json(name = "tertiary_color_start") val tertiaryColorStart: String? = null,
    @Json(name = "primary_text_color") val primaryTextColor: String? = null,
    @Json(name = "secondary_menu_color") val secondaryMenuColor: String? = null,
    @Json(name = "active_color") val activeColor: String? = null,
    @Json(name = "deactive_color") val deactiveColor: String? = null,
    @Json(name = "secondary_color") val secondaryColor: String? = null,
    @Json(name = "tertiary_color_end") val tertiaryColorEnd: String? = null,
    @Json(name = "fourth_text_color") val fourthTextColor: String? = null,
    @Json(name = "tertiary_text_color") val tertiaryTextColor: String? = null,
    @Json(name = "secondary_text_color") val secondaryTextColor: String? = null,
    @Json(name = "fourth_color") val fourthColor: String? = null

) {

    suspend fun toTheme(): Theme? =
        Either.catch {

            Theme(
                primaryColorStart?.toHEXColor()!!,
                primaryColorEnd?.toHEXColor()!!,
                secondaryColor?.toHEXColor()!!,
                tertiaryColorStart?.toHEXColor()!!,
                tertiaryColorEnd?.toHEXColor()!!,
                primaryTextColor?.toHEXColor()!!,
                secondaryTextColor?.toHEXColor()!!,
                tertiaryTextColor?.toHEXColor()!!,
                fourthTextColor?.toHEXColor()!!,
                primaryMenuColor?.toHEXColor()!!,
                secondaryMenuColor?.toHEXColor()!!,
                activeColor?.toHEXColor()!!,
                deactiveColor?.toHEXColor()!!,
                fourthColor?.toHEXColor()!!
            )

        }.orNull()
}