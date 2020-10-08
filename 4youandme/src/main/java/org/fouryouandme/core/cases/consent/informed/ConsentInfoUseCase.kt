package org.fouryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.ConsentInfoModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.consent.informed.ConsentInfoRepository.fetchConsent
import org.fouryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoUseCase {

    suspend fun ConsentInfoModule.getConsent(): Either<FourYouAndMeError, ConsentInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchConsent(it, environment.studyId()) }

}