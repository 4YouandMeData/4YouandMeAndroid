package com.foryouandme.core.cases.survey

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.SurveyModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.survey.Survey

object SurveyRepository {

    internal suspend fun SurveyModule.fetchSurvey(
        token: String,
        surveyId: String
    ): Either<ForYouAndMeError, Survey?> =
        suspend { api.getSurvey(token, surveyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.toSurvey() }

}