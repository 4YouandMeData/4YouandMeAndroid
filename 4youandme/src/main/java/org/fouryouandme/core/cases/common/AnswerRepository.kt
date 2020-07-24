package org.fouryouandme.core.cases.common

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.common.request.AnswerDataRequest
import org.fouryouandme.core.data.api.common.request.AnswersRequest
import org.fouryouandme.core.ext.unwrapToEither

object AnswerRepository {

    internal fun <F> sendAnswer(
        runtime: Runtime<F>,
        token: String,
        questionId: String,
        answerText: String,
        batchCode: String,
        possibleAnswerId: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {
            !runtime.injector.answerApi
                .sendAnswers(
                    token, questionId, AnswersRequest(
                        AnswerDataRequest(answerText, batchCode, possibleAnswerId)
                    )
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
    }
}