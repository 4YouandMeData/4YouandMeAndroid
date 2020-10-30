package com.foryouandme.core.cases.screening

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.ScreeningModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.screening.ScreeningRepository.fetchScreening
import com.foryouandme.core.entity.screening.Screening

object ScreeningUseCase {

    suspend fun ScreeningModule.getScreening(): Either<ForYouAndMeError, Screening?> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchScreening(it, environment.studyId()) }

}