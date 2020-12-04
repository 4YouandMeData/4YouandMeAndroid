package com.foryouandme.entity.integration

sealed class IntegrationApp(val identifier: String, val packageName: String) {

    object Oura : IntegrationApp("oura", "com.ouraring.oura")
    object Fitbit : IntegrationApp("fitbit", "com.fitbit.FitbitMobile")
    object Garmin : IntegrationApp("garmin", "com.garmin.android.apps.connectmobile")
    object RescueTime : IntegrationApp("rescuetime", "com.rescuetime.android")
    object Instagram : IntegrationApp("instagram", "instagram")
    object Twitter : IntegrationApp("twitter", "twitter")


    companion object {

        fun fromIdentifier(identifier: String): IntegrationApp? =
            when {
                identifier.contains("oura", true) -> Oura
                identifier.contains("fitbit", true) -> Fitbit
                identifier.contains("garmin", true) -> Garmin
                identifier.contains("rescuetime", true) -> RescueTime
                identifier.contains("instagram", true) -> Instagram
                identifier.contains("twitter", true) -> Twitter
                else -> null
            }

    }

}