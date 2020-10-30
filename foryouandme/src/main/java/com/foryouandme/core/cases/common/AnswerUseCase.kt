package com.foryouandme.core.cases.common

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.AnswerModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.common.AnswerRepository.uploadAnswer
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


object AnswerUseCase {

    internal suspend fun AnswerModule.sendAnswer(
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ): Either<ForYouAndMeError, Unit> =

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