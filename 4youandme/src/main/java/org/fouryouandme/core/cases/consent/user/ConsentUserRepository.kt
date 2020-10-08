package org.fouryouandme.core.cases.consent.user

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.ConsentUserModule
import org.fouryouandme.core.arch.deps.modules.unwrapResponse
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.consent.user.request.ConfirmUserConsentEmailRequest
import org.fouryouandme.core.data.api.consent.user.request.CreateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UpdateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UserConsentRequest
import org.fouryouandme.core.entity.consent.user.ConsentUser

object ConsentUserRepository {

    internal suspend fun ConsentUserModule.fetchConsent(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, ConsentUser?> =
        suspend { api.getConsentUser(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toConsentUser(it) }

    internal suspend fun ConsentUserModule.createUserConsent(
        token: String,
        studyId: String,
        email: String
    ): Either<FourYouAndMeError, Unit> =
        UserConsentRequest(CreateUserConsentRequest(true, email))
            .pipe { suspend { api.createUserConsent(token, studyId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

    internal suspend fun ConsentUserModule.updateUserConsent(
        token: String,
        studyId: String,
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Either<FourYouAndMeError, Unit> =
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
    ): Either<FourYouAndMeError, Unit?> =
        UserConsentRequest(ConfirmUserConsentEmailRequest(code))
            .pipe { suspend { api.confirmEmail(token, studyId, it) } }
            .pipe { errorModule.unwrapToEither(it) }
            .pipe { errorModule.unwrapResponse(it) }

    internal suspend fun ConsentUserModule.resendConfirmationEmail(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, Unit?> =
        suspend { api.resendConfirmationEmail(token, studyId) }
            .pipe { errorModule.unwrapToEither(it) }
            .pipe { errorModule.unwrapResponse(it) }


}