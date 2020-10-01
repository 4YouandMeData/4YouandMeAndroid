package org.fouryouandme.core.cases.yourdata

import arrow.core.Either
import org.fouryouandme.core.arch.deps.modules.YourDataModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.yourdata.YourData

object YourDataRepository {

    suspend fun YourDataModule.fetchYourData(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, YourData?> =
        errorModule.unwrapToEither { api.getYourData(token, studyId) }
            .map { it.toYourData() }

}