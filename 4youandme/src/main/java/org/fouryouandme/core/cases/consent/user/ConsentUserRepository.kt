package org.fouryouandme.core.cases.consent.user

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.consent.user.request.ConfirmUserConsentEmailRequest
import org.fouryouandme.core.data.api.consent.user.request.CreateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UpdateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UserConsentRequest
import org.fouryouandme.core.entity.consent.user.ConsentUser
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapEmptyToEither
import org.fouryouandme.core.ext.unwrapToEither

object ConsentUserRepository {

    internal fun <F> getConsent(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, ConsentUser>> =
        runtime.fx.concurrent {

            !runtime.injector.consentUserApi
                .getConsentUser(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get().toConsentUser(it) }
                .noneToError(runtime)

        }

    internal fun <F> createUserConsent(
        runtime: Runtime<F>,
        token: String,
        studyId: String,
        email: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            !runtime.injector.consentUserApi
                .createUserConsent(
                    token,
                    studyId,
                    UserConsentRequest(CreateUserConsentRequest(true, email))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)

        }

    internal fun <F> updateUserConsent(
        runtime: Runtime<F>,
        token: String,
        studyId: String,
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            !runtime.injector.consentUserApi
                .updateUserConsent(
                    token,
                    studyId,
                    UserConsentRequest(
                        UpdateUserConsentRequest(
                            firstName,
                            lastName,
                            signatureBase64
                        )
                    )
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)

        }

    internal fun <F> confirmEmail(
        runtime: Runtime<F>,
        token: String,
        studyId: String,
        code: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            !runtime.injector.consentUserApi
                .confirmEmail(
                    token,
                    studyId,
                    UserConsentRequest(ConfirmUserConsentEmailRequest(code))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapEmptyToEither(runtime)

        }

    internal fun <F> resendConfirmationEmail(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            !runtime.injector.consentUserApi
                .resendConfirmationEmail(
                    token,
                    studyId
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapEmptyToEither(runtime)

        }
}