package org.fouryouandme.core.cases.integration

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.IntegrationModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.integration.IntegrationRepository.fetchIntegration
import org.fouryouandme.core.entity.integration.Integration

object IntegrationUseCase {

    suspend fun IntegrationModule.getIntegration(): Either<FourYouAndMeError, Integration?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchIntegration(it, environment.studyId()) }
}