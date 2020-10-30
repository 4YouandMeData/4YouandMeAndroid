package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.consent.informed.ConsentInfoApi

data class ConsentInfoModule(
    val api: ConsentInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)