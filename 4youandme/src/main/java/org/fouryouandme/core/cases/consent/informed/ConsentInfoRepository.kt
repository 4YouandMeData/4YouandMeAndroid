package org.fouryouandme.core.cases.consent.informed

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.ConsentInfoModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.consent.informed.ConsentInfo

object ConsentInfoRepository {

    internal suspend fun ConsentInfoModule.fetchConsent(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, ConsentInfo?> =
        suspend { api.getConsent(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toConsentInfo(it) }

}