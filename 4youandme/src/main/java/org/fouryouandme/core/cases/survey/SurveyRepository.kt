package org.fouryouandme.core.cases.survey

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.SurveyModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.survey.Survey

object SurveyRepository {

    internal suspend fun SurveyModule.fetchSurvey(
        token: String,
        surveyId: String
    ): Either<FourYouAndMeError, Survey?> =
        suspend { api.getSurvey(token, surveyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.toSurvey() }

}