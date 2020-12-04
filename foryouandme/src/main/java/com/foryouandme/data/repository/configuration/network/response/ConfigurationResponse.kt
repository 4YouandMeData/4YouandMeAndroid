package com.foryouandme.data.repository.configuration.network.response

import com.foryouandme.entity.configuration.Configuration
import com.squareup.moshi.Json

data class ConfigurationResponse(

    @Json(name = "strings") val strings: StringsResponse? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "study_id") val studyId: Int? = null,
    @Json(name = "color_palette") val colorPalette: ColorPaletteResponse? = null,
    @Json(name = "country_codes") val countryCodes: List<String>? = null

) {

    fun toConfiguration(): Configuration? {

        val theme = colorPalette?.toTheme()
        val text = strings?.toText()

        return when (null) {
            theme, text -> null
            else ->
                Configuration(
                    theme,
                    text,
                    countryCodes ?: emptyList()
                )
        }

    }

}
