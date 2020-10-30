package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.optins.OptInsApi

data class OptInModule(
    val api: OptInsApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)