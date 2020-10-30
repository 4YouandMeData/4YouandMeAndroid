package com.fouryouandme.core.cases.common

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.AnswerModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.common.AnswerRepository.uploadAnswer
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