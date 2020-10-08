package org.fouryouandme.core.cases.common

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.AnswerModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.common.AnswerRepository.uploadAnswer
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


object AnswerUseCase {

    internal suspend fun AnswerModule.sendAnswer(
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ): Either<FourYouAndMeError, Unit> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {

                uploadAnswer(
                    it,
                    questionId,
                    answerText,
                    ZonedDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")),
                    possibleAnswerId
                )

            }
}