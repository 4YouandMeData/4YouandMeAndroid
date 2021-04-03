package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.auth.screening.network.ScreeningApi

data class ScreeningModule(
    val api: ScreeningApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)