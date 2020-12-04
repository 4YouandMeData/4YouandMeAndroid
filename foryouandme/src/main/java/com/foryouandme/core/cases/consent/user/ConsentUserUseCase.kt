package com.foryouandme.core.cases.consent.user

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.ConsentUserModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.consent.user.ConsentUserRepository.confirmEmail
import com.foryouandme.core.cases.consent.user.ConsentUserRepository.createUserConsent
import com.foryouandme.core.cases.consent.user.ConsentUserRepository.fetchConsent
import com.foryouandme.core.cases.consent.user.ConsentUserRepository.resendConfirmationEmail
import com.foryouandme.core.cases.consent.user.ConsentUserRepository.updateUserConsent
import com.foryouandme.entity.consent.user.ConsentUser

object ConsentUserUseCase {

    suspend fun ConsentUserModule.getConsent(): Either<ForYouAndMeError, ConsentUser?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchConsent(it, environment.studyId()) }

    suspend fun ConsentUserModule.createUserConsent(
        email: String
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                createUserConsent(it, environment.studyId(), email)
            }

    internal suspend fun ConsentUserModule.updateUserConsent(
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                updateUserConsent(it, environment.studyId(), firstName, lastName, signatureBase64)
            }

    internal suspend fun ConsentUserModule.confirmEmail(
        code: String
    ): Either<ForYouAndMeError, Unit?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                confirmEmail(it, environment.studyId(), code)
            }

    internal suspend fun ConsentUserModule.resendConfirmationEmail(
    ): Either<ForYouAndMeError, Unit?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                resendConfirmationEmail(it, environment.studyId())
            }

}