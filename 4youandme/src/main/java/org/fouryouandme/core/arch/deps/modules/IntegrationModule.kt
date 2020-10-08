package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.integration.IntegrationApi

data class IntegrationModule(
    val api: IntegrationApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)