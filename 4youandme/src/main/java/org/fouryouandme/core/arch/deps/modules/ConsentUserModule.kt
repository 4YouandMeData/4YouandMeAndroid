package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.consent.user.ConsentUserApi

data class ConsentUserModule(
    val api: ConsentUserApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)