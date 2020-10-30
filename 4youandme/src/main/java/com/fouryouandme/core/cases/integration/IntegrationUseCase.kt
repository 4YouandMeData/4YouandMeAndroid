package com.fouryouandme.core.cases.integration

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.IntegrationModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.integration.IntegrationRepository.fetchIntegration
import com.fouryouandme.core.entity.integration.Integration

object IntegrationUseCase {

    suspend fun IntegrationModule.getIntegration(): Either<FourYouAndMeError, Integration?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchIntegration(it, environment.studyId()) }
}