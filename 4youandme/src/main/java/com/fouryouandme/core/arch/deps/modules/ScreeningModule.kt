package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.screening.ScreeningApi

data class ScreeningModule(
    val api: ScreeningApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)