package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.yourdata.YourDataApi

data class YourDataModule(
    val api: YourDataApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)