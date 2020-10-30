package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.consent.review.ConsentReviewApi

data class ConsentReviewModule(
    val api: ConsentReviewApi,
    val errorModule: ErrorModule,
    val authModule: AuthModule,
    val environment: Environment
)