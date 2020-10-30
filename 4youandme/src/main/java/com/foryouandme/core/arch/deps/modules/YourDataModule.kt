package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.yourdata.YourDataApi

data class YourDataModule(
    val api: YourDataApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)