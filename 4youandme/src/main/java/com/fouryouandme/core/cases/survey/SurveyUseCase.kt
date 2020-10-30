package com.fouryouandme.core.cases.survey

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.SurveyModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.survey.SurveyRepository.fetchSurvey
import com.fouryouandme.core.entity.survey.Survey

object SurveyUseCase {

    suspend fun SurveyModule.getSurvey(
        surveyId: String
    ): Either<FourYouAndMeError, Survey?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchSurvey(it, surveyId) }

}