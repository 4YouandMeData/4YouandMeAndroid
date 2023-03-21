package com.foryouandme.entity.configuration

import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.phase.StudyPhase

data class Configuration(
    val theme: Theme,
    val text: Text,
    val countryCodes: List<String>,
    private val integrationsIdentifiers: List<String>,
    val pinCodeLogin: Boolean,
    val phases: List<StudyPhase>
) {

    val integrations: List<IntegrationApp>
        get() = integrationsIdentifiers.mapNotNull { IntegrationApp.fromIdentifier(it) }


    companion object {

        fun mock(): Configuration =
            Configuration(
                theme = Theme.mock(),
                text = Text.mock(),
                countryCodes = emptyList(),
                integrationsIdentifiers = emptyList(),
                pinCodeLogin = false,
                phases = emptyList()
            )

    }

}