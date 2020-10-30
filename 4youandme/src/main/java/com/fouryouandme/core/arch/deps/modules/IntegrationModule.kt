package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.integration.IntegrationApi

data class IntegrationModule(
    val api: IntegrationApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)