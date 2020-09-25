package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.consent.review.ConsentReviewApi

data class ConsentReviewModule(
    val api: ConsentReviewApi,
    val errorModule: ErrorModule,
    val authModule: AuthModule,
    val environment: Environment
)