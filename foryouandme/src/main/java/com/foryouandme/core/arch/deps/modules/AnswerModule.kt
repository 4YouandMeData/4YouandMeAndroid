package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.common.AnswerApi

data class AnswerModule(
    val api: AnswerApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)