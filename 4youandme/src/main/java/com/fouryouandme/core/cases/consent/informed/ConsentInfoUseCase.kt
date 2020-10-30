package com.fouryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.ConsentInfoModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.consent.informed.ConsentInfoRepository.fetchConsent
import com.fouryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoUseCase {

    suspend fun ConsentInfoModule.getConsent(): Either<FourYouAndMeError, ConsentInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchConsent(it, environment.studyId()) }

}