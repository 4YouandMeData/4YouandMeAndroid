package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.data.api.consent.informed.ConsentInfoApi

data class ConsentInfoModule(
    val api: ConsentInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)