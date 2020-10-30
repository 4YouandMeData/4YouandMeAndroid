package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.consent.informed.ConsentInfoApi

data class ConsentInfoModule(
    val api: ConsentInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)