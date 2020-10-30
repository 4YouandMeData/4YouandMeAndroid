package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.screening.ScreeningApi

data class ScreeningModule(
    val api: ScreeningApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)