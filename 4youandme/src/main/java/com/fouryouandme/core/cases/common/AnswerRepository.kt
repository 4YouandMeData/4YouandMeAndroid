package com.fouryouandme.core.cases.common

import arrow.core.Either
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.AnswerModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.data.api.common.request.AnswerDataRequest
import com.fouryouandme.core.data.api.common.request.AnswersRequest

object AnswerRepository {

    internal suspend fun AnswerModule.uploadAnswer(
        token: String,
        questionId: String,
        answerText: String,
        batchCode: String,
        possibleAnswerId: String
    ): Either<FourYouAndMeError, Unit> =

        AnswersRequest(AnswerDataRequest(answerText, batchCode, possibleAnswerId))
            .pipe { suspend { api.sendAnswers(token, questionId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

}