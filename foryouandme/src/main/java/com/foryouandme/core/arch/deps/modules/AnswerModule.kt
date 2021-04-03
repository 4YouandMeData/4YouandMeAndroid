package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.auth.answer.network.AuthAnswerApi

data class AnswerModule(
    val api: AuthAnswerApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)