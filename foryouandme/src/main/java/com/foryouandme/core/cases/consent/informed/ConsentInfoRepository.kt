package com.foryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.ConsentInfoModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoRepository {

    internal suspend fun ConsentInfoModule.fetchConsent(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, ConsentInfo?> =
        suspend { api.getConsent(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toConsentInfo(it) }

}