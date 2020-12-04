package com.foryouandme.data.repository.configuration.network.response

import com.foryouandme.entity.integration.IntegrationApp
import com.squareup.moshi.Json

data class SupportedIntegrationResponse(
    @Json(name = "instagram") val instagram: IntegrationResponse? = null,
    @Json(name = "twitter") val twitter: IntegrationResponse? = null,
    @Json(name = "camcog") val camcog: IntegrationResponse? = null,
    @Json(name = "rescuetime") val rescuetime: IntegrationResponse? = null,
    @Json(name = "oura") val oura: IntegrationResponse? = null,
    @Json(name = "garmin") val garmin: IntegrationResponse? = null,
    @Json(name = "fitbit") val fitbit: IntegrationResponse? = null,
    @Json(name = "bodyport") val bodyport: IntegrationResponse? = null,
) {

    fun toIntegrationAppsIdentifiers(): List<String> =

        mutableListOf<String>()
            .apply {

                instagram?.let { add(IntegrationApp.Instagram.identifier) }
                twitter?.let { add(IntegrationApp.Twitter.identifier) }
                rescuetime?.let { add(IntegrationApp.RescueTime.identifier) }
                oura?.let { add(IntegrationApp.Oura.identifier) }
                garmin?.let { add(IntegrationApp.Garmin.identifier) }
                fitbit?.let { add(IntegrationApp.Fitbit.identifier) }

            }

}

data class IntegrationResponse(
    @Json(name = "oauth") val oauth: Boolean? = null,
    @Json(name = "biometric") val biometric: Boolean? = null
)