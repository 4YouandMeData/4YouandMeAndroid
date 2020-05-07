package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.Configuration

data class ConfigurationResponse(

    @Json(name = "strings") val strings: StringsResponse? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "study_id") val studyId: Int? = null,
    @Json(name = "color_palette") val colorPalette: ColorPaletteResponse? = null

) {

    fun toConfiguration(): Option<Configuration> =
        Option.fx {

            Configuration(
                colorPalette.toOption().bind().toTheme().bind(),
                strings.toOption().bind().toText().bind()
            )

        }
}
