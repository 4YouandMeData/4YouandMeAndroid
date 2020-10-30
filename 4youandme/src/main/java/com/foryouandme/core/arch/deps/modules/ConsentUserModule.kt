package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.consent.user.ConsentUserApi

data class ConsentUserModule(
    val api: ConsentUserApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)