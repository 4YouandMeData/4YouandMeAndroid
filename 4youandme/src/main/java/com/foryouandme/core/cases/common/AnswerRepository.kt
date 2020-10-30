package com.foryouandme.core.cases.common

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.AnswerModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.data.api.common.request.AnswerDataRequest
import com.foryouandme.core.data.api.common.request.AnswersRequest

object AnswerRepository {

    internal suspend fun AnswerModule.uploadAnswer(
        token: String,
        questionId: String,
        answerText: String,
        batchCode: String,
        possibleAnswerId: String
    ): Either<ForYouAndMeError, Unit> =

        AnswersRequest(AnswerDataRequest(answerText, batchCode, possibleAnswerId))
            .pipe { suspend { api.sendAnswers(token, questionId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

}