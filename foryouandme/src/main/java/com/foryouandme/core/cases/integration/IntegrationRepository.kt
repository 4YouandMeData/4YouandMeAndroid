package com.foryouandme.core.cases.integration

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.IntegrationModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.entity.integration.Integration

object IntegrationRepository {

    internal suspend fun IntegrationModule.fetchIntegration(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, Integration?> =
        suspend { api.getIntegration(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toIntegration(it) }

}