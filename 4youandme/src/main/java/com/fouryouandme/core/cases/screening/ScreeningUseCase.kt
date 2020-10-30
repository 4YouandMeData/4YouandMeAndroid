package com.fouryouandme.core.cases.screening

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.ScreeningModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.screening.ScreeningRepository.fetchScreening
import com.fouryouandme.core.entity.screening.Screening

object ScreeningUseCase {

    suspend fun ScreeningModule.getScreening(): Either<FourYouAndMeError, Screening?> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchScreening(it, environment.studyId()) }

}