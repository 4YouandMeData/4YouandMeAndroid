package com.foryouandme.core.ext

import com.foryouandme.core.entity.configuration.Configuration

sealed class StudyIntegration(val identifier: String) {

    object Garmin : StudyIntegration("garmin")
    object Fitbit : StudyIntegration("fitbit")
    object Oura : StudyIntegration("oura")
    object Instagram : StudyIntegration("instagram")
    object RescueTime : StudyIntegration("rescuetime")
    object Twitter : StudyIntegration("twitter")

    companion object {
        fun fromIdentifier(identifier: String): StudyIntegration? =
            when (identifier) {
                Garmin.identifier -> Garmin
                Fitbit.identifier -> Fitbit
                Oura.identifier -> Oura
                Instagram.identifier -> Instagram
                RescueTime.identifier -> RescueTime
                Twitter.identifier -> Twitter
                else -> null
            }
    }

}

fun Configuration.getStudyIntegrations(): List<StudyIntegration> =
    text.profile.oauth
        .split(";")
        .map { it.trim() }
        .mapNotNull { StudyIntegration.fromIdentifier(it) }

