package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import arrow.core.getOrElse
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
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
        phone: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val configuration =
                ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                    .bind()
                    .toOption()

            !runtime.injector.authApi
                .verifyPhoneNumber(
                    runtime.injector.environment.studyId(),
                    PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapEmptyToEither(runtime)
                .mapError(runtime.fx) { error ->

                    if (error is FourYouAndMeError.NetworkErrorHTTP && error.code == 404)
                        FourYouAndMeError.MissingPhoneNumber {
                            configuration
                                .map { it.text.phoneVerification.error.errorMissingNumber }
                                .getOrElse {
                                    runtime.app.getString(R.string.ERROR_generic)
                                }
                        }
                    else
                        error
                }
        }

    fun <F> login(
        runtime: Runtime<F>,
        phone: String,
        code: String
    ): Kind<F, Either<FourYouAndMeError, UserResponse>> =
        runtime.fx.concurrent {

            val configuration =
                ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                    .bind()
                    .toOption()

            !runtime.injector.authApi
                .login(
                    runtime.injector.environment.studyId(),
                    LoginRequest(PhoneLoginRequest(phone, code))
                )
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapError(runtime.fx) { error ->

                    if (error is FourYouAndMeError.NetworkErrorHTTP && error.code == 403)
                        FourYouAndMeError.WrongPhoneCode {
                            configuration
                                .map { it.text.phoneVerification.error.errorWrongCode }
                                .getOrElse {
                                    runtime.app.getString(R.string.ERROR_generic)
                                }
                        }
                    else
                        error
                }
        }
}