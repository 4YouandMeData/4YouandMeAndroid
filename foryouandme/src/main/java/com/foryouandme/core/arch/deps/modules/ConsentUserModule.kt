package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.consent.user.network.ConsentUserApi

data class ConsentUserModule(
    val api: ConsentUserApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)