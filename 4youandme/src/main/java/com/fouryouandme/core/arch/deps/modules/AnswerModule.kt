package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.common.AnswerApi

data class AnswerModule(
    val api: AnswerApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)