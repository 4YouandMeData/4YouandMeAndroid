package org.fouryouandme.core.cases.common

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.ext.foldToKindEither
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object AnswerUseCase {
    internal fun <F> sendAnswer(
        runtime: Runtime<F>,
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {
            val token = !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            val tz: TimeZone = TimeZone.getTimeZone("UTC")
            val df: DateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm'Z'",
                Locale.getDefault()
            )
            df.timeZone = tz
            val nowAsISO: String = df.format(Date())

            !token.foldToKindEither(runtime.fx) {
                AnswerRepository.sendAnswer(
                    runtime,
                    it,
                    questionId,
                    answerText,
                    nowAsISO,
                    possibleAnswerId
                )
            }
        }
}