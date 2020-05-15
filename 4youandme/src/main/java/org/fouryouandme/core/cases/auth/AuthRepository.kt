package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.auth.request.PhoneNumberRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.ext.mapError
import org.fouryouandme.core.ext.unwrapToEither

object AuthRepository {

    fun <F> verifyPhoneNumber(
        runtime: Runtime<F>,
        phone: String,
        missingPhoneErrorMessage: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            !runtime.injector.authApi
                .verifyPhoneNumber(
                    runtime.injector.environment.studyId(),
                    PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapError(runtime.fx) {

                    if (it is FourYouAndMeError.NetworkErrorHTTP && it.code == 404)
                        FourYouAndMeError.MissingPhoneNumber { missingPhoneErrorMessage }
                    else
                        it
                }
        }
}