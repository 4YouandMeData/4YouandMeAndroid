package com.foryouandme.core.entity.integration

sealed class IntegrationApp(val packageName: String) {

    object Oura : IntegrationApp("com.ouraring.oura")
    object Fitbit : IntegrationApp("com.fitbit.FitbitMobile")
    object Garmin : IntegrationApp("com.garmin.android.apps.connectmobile")
    object RescueTime : IntegrationApp("com.rescuetime.android")
    object Instagram : IntegrationApp("instagram")
    object Twitter : IntegrationApp("twitter")


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