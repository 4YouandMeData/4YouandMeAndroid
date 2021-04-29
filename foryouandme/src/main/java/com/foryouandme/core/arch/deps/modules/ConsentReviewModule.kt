package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.auth.consent.network.ConsentApi

data class ConsentReviewModule(
    val api: ConsentApi,
    val errorModule: ErrorModule,
    val authModule: AuthModule,
    val environment: Environment
)