package com.fouryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.ConsentInfoModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoRepository {

    internal suspend fun ConsentInfoModule.fetchConsent(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, ConsentInfo?> =
        suspend { api.getConsent(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toConsentInfo(it) }

}