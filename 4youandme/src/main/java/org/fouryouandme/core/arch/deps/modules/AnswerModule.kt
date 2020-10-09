package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.common.AnswerApi

data class AnswerModule(
    val api: AnswerApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)