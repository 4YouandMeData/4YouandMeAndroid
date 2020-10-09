package org.fouryouandme.core.cases.screening

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.ScreeningModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.screening.Screening

object ScreeningRepository {

    suspend fun ScreeningModule.fetchScreening(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, Screening?> =

        suspend { api.getScreening(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toScreening(it) }

}