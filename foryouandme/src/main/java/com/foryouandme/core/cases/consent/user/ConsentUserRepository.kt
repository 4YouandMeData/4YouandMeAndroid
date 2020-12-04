package com.foryouandme.core.cases.consent.user

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.ConsentUserModule
import com.foryouandme.core.arch.deps.modules.unwrapResponse
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.data.api.consent.user.request.ConfirmUserConsentEmailRequest
import com.foryouandme.core.data.api.consent.user.request.CreateUserConsentRequest
import com.foryouandme.core.data.api.consent.user.request.UpdateUserConsentRequest
import com.foryouandme.core.data.api.consent.user.request.UserConsentRequest
import com.foryouandme.entity.consent.user.ConsentUser

object ConsentUserRepository {

    internal suspend fun ConsentUserModule.fetchConsent(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, ConsentUser?> =
        suspend { api.getConsentUser(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toConsentUser(it) }

    internal suspend fun ConsentUserModule.createUserConsent(
        token: String,
        studyId: String,
        email: String
    ): Either<ForYouAndMeError, Unit> =
        UserConsentRequest(CreateUserConsentRequest(true, email))
            .pipe { suspend { api.createUserConsent(token, studyId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

    internal suspend fun ConsentUserModule.updateUserConsent(
        token: String,
        studyId: String,
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Either<ForYouAndMeError, Unit> =
        UserConsentRequest(
            UpdateUserConsentRequest(
                firstName,
                lastName,
                "data:image/png;base64,\\$signatureBase64"
            )
        ).pipe { suspend { api.updateUserConsent(token, studyId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

    internal suspend fun ConsentUserModule.confirmEmail(
        token: String,
        studyId: String,
        code: String
    ): Either<ForYouAndMeError, Unit?> =
        UserConsentRequest(ConfirmUserConsentEmailRequest(code))
            .pipe { suspend { api.confirmEmail(token, studyId, it) } }
            .pipe { errorModule.unwrapToEither(it) }
            .pipe { errorModule.unwrapResponse(it) }

    internal suspend fun ConsentUserModule.resendConfirmationEmail(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, Unit?> =
        suspend { api.resendConfirmationEmail(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .pipe { errorModule.unwrapResponse(it) }


}