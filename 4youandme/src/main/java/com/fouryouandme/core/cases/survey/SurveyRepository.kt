package com.fouryouandme.core.cases.survey

import arrow.core.Either
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.SurveyModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.survey.Survey

object SurveyRepository {

    internal suspend fun SurveyModule.fetchSurvey(
        token: String,
        surveyId: String
    ): Either<FourYouAndMeError, Survey?> =
        suspend { api.getSurvey(token, surveyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.toSurvey() }

}