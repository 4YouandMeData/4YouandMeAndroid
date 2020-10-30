package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.consent.user.ConsentUserApi

data class ConsentUserModule(
    val api: ConsentUserApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)