package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.optins.OptInsApi

data class OptInModule(
    val api: OptInsApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)