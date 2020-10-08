package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.screening.ScreeningApi

data class ScreeningModule(
    val api: ScreeningApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)