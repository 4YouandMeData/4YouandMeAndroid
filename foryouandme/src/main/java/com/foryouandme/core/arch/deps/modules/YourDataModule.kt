package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.yourdata.network.YourDataApi

data class YourDataModule(
    val api: YourDataApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)