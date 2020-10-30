package com.fouryouandme.core.cases.consent.user

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.ConsentUserModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.consent.user.ConsentUserRepository.confirmEmail
import com.fouryouandme.core.cases.consent.user.ConsentUserRepository.createUserConsent
import com.fouryouandme.core.cases.consent.user.ConsentUserRepository.fetchConsent
import com.fouryouandme.core.cases.consent.user.ConsentUserRepository.resendConfirmationEmail
import com.fouryouandme.core.cases.consent.user.ConsentUserRepository.updateUserConsent
import com.fouryouandme.core.entity.consent.user.ConsentUser

object ConsentUserUseCase {

    suspend fun ConsentUserModule.getConsent(): Either<FourYouAndMeError, ConsentUser?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchConsent(it, environment.studyId()) }

    suspend fun ConsentUserModule.createUserConsent(
        email: String
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                createUserConsent(it, environment.studyId(), email)
            }

    internal suspend fun ConsentUserModule.updateUserConsent(
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                updateUserConsent(it, environment.studyId(), firstName, lastName, signatureBase64)
            }

    internal suspend fun ConsentUserModule.confirmEmail(
        code: String
    ): Either<FourYouAndMeError, Unit?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                confirmEmail(it, environment.studyId(), code)
            }

    internal suspend fun ConsentUserModule.resendConfirmationEmail(
    ): Either<FourYouAndMeError, Unit?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                resendConfirmationEmail(it, environment.studyId())
            }

}