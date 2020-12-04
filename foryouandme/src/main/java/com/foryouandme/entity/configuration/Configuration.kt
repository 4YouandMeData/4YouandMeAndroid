package com.foryouandme.entity.configuration

import com.foryouandme.entity.integration.IntegrationApp

data class Configuration(
    val theme: Theme,
    val text: Text,
    val countryCodes: List<String>,
    private val integrationsIdentifiers: List<String>
) {

    val integrations: List<IntegrationApp>
        get() = integrationsIdentifiers.mapNotNull { IntegrationApp.fromIdentifier(it) }

}