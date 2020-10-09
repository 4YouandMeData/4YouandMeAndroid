package org.fouryouandme.core.cases.integration

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.IntegrationModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.integration.Integration

object IntegrationRepository {

    internal suspend fun IntegrationModule.fetchIntegration(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, Integration?> =
        suspend { api.getIntegration(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toIntegration(it) }

}