package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.auth.request.LoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneLoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.data.api.auth.response.UserResponse
import org.fouryouandme.core.ext.mapError
import org.fouryouandme.core.ext.unwrapEmptyToEither
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
                .unwrapEmptyToEither(runtime)
                .mapError(runtime.fx) {

                    if (it is FourYouAndMeError.NetworkErrorHTTP && it.code == 404)
                        FourYouAndMeError.MissingPhoneNumber { missingPhoneErrorMessage }
                    else
                        it
                }
        }

    fun <F> login(
        runtime: Runtime<F>,
        phone: String,
        code: String,
        wrongCodeErrorMessage: String
    ): Kind<F, Either<FourYouAndMeError, UserResponse>> =
        runtime.fx.concurrent {

            !runtime.injector.authApi
                .login(
                    runtime.injector.environment.studyId(),
                    LoginRequest(PhoneLoginRequest(phone, code))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapError(runtime.fx) {

                    if (it is FourYouAndMeError.NetworkErrorHTTP && it.code == 403)
                        FourYouAndMeError.WrongPhoneCode { wrongCodeErrorMessage }
                    else
                        it
                }
        }
}