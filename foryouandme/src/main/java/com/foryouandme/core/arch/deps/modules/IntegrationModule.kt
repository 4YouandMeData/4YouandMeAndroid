package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.data.api.integration.IntegrationApi

data class IntegrationModule(
    val api: IntegrationApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)