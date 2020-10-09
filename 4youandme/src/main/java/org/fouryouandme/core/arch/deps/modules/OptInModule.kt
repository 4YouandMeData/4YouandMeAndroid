package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.optins.OptInsApi

data class OptInModule(
    val api: OptInsApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)