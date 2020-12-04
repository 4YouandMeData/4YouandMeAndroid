package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.data.api.screening.ScreeningApi

data class ScreeningModule(
    val api: ScreeningApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)