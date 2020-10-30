package com.foryouandme.core.arch.deps.modules

import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.consent.review.ConsentReviewApi

data class ConsentReviewModule(
    val api: ConsentReviewApi,
    val errorModule: ErrorModule,
    val authModule: AuthModule,
    val environment: Environment
)