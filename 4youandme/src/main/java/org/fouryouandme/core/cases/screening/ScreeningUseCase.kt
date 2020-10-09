package org.fouryouandme.core.cases.screening

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.ScreeningModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.screening.ScreeningRepository.fetchScreening
import org.fouryouandme.core.entity.screening.Screening

object ScreeningUseCase {

    suspend fun ScreeningModule.getScreening(): Either<FourYouAndMeError, Screening?> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchScreening(it, environment.studyId()) }

}