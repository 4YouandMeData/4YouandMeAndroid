package com.fouryouandme.core.cases.screening

import arrow.core.Either
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.ScreeningModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.screening.Screening

object ScreeningRepository {

    suspend fun ScreeningModule.fetchScreening(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, Screening?> =

        suspend { api.getScreening(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toScreening(it) }

}