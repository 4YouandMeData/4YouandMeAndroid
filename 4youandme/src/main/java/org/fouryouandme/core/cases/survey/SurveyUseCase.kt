package org.fouryouandme.core.cases.survey

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.SurveyModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.survey.SurveyRepository.fetchSurvey
import org.fouryouandme.core.entity.survey.Survey

object SurveyUseCase {

    suspend fun SurveyModule.getSurvey(
        surveyId: String
    ): Either<FourYouAndMeError, Survey?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchSurvey(it, surveyId) }

}