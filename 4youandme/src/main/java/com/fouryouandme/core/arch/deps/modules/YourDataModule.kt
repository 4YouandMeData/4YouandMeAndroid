package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.yourdata.YourDataApi

data class YourDataModule(
    val api: YourDataApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)