package com.foryouandme.core.cases.screening

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.ScreeningModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.screening.Screening

object ScreeningRepository {

    suspend fun ScreeningModule.fetchScreening(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, Screening?> =

        suspend { api.getScreening(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toScreening(it) }

}