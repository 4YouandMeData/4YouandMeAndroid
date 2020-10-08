package org.fouryouandme.core.cases.consent.user

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.ConsentUserModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.consent.user.ConsentUserRepository.confirmEmail
import org.fouryouandme.core.cases.consent.user.ConsentUserRepository.createUserConsent
import org.fouryouandme.core.cases.consent.user.ConsentUserRepository.fetchConsent
import org.fouryouandme.core.cases.consent.user.ConsentUserRepository.resendConfirmationEmail
import org.fouryouandme.core.cases.consent.user.ConsentUserRepository.updateUserConsent
import org.fouryouandme.core.entity.consent.user.ConsentUser

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