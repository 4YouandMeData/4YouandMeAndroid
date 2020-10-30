package com.foryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.ConsentInfoModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.consent.informed.ConsentInfoRepository.fetchConsent
import com.foryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoUseCase {

    suspend fun ConsentInfoModule.getConsent(): Either<ForYouAndMeError, ConsentInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchConsent(it, environment.studyId()) }

}