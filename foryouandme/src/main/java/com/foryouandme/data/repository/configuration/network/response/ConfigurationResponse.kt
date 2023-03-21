package com.foryouandme.data.repository.configuration.network.response

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.phase.StudyPhase
import com.squareup.moshi.Json

data class ConfigurationResponse(

    @Json(name = "strings") val strings: StringsResponse? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "study_id") val studyId: Int? = null,
    @Json(name = "color_palette") val colorPalette: ColorPaletteResponse? = null,
    @Json(name = "country_codes") val countryCodes: List<String>? = null,
    @Json(name = "supported_integrations") val integration: SupportedIntegrationResponse? = null,
    @Json(name = "pincode_login") val pinCodeLogin: Boolean? = null

) {

    fun toConfiguration(phases: List<StudyPhase>): Configuration? {

        val theme = colorPalette?.toTheme()
        val text = strings?.toText()

        return when (null) {
            theme, text, pinCodeLogin -> null
            else ->
                Configuration(
                    theme = theme,
                    text = text,
                    countryCodes = countryCodes ?: emptyList(),
                    integrationsIdentifiers =
                    integration?.toIntegrationAppsIdentifiers() ?: emptyList(),
                    pinCodeLogin = pinCodeLogin,
                    phases = phases
                )
        }

    }

}
