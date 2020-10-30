package com.foryouandme.core.cases.integration

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.IntegrationModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.integration.IntegrationRepository.fetchIntegration
import com.foryouandme.core.entity.integration.Integration

object IntegrationUseCase {

    suspend fun IntegrationModule.getIntegration(): Either<ForYouAndMeError, Integration?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchIntegration(it, environment.studyId()) }
}