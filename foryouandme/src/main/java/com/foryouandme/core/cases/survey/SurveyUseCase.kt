package com.foryouandme.core.cases.survey

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.SurveyModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.survey.SurveyRepository.fetchSurvey
import com.foryouandme.entity.survey.Survey

object SurveyUseCase {

    suspend fun SurveyModule.getSurvey(
        surveyId: String
    ): Either<ForYouAndMeError, Survey?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchSurvey(it, surveyId) }

}