package com.foryouandme.core.ext

import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.integration.IntegrationApp

fun Configuration.getAppIntegrations(): List<IntegrationApp> =
    text.profile.oauth
        .split(";")
        .map { it.trim() }
        .mapNotNull { IntegrationApp.fromIdentifier(it) }

