package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Either
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.Configuration

data class ConfigurationResponse(

    @Json(name = "strings") val strings: StringsResponse? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "study_id") val studyId: Int? = null,
    @Json(name = "color_palette") val colorPalette: ColorPaletteResponse? = null,
    @Json(name = "country_codes") val countryCodes: List<String>? =  null

) {

    suspend fun toConfiguration(): Configuration? =
        Either.catch {

            Configuration(
                colorPalette?.toTheme()!!,
                strings?.toText()!!,
                countryCodes ?: emptyList()
            )

        }.orNull()
}
