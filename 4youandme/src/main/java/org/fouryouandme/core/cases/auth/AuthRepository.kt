package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.*
import arrow.core.extensions.either.applicativeError.applicativeError
import arrow.integrations.retrofit.adapter.unwrapBody
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.toFourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.auth.request.LoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneLoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.data.api.auth.response.UserResponse
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.mapError
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapEmptyToEither
import retrofit2.Response

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
    ): Kind<F, Either<FourYouAndMeError, User>> =
        runtime.fx.concurrent {

            val configuration =
                ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                    .bind()
                    .toOption()

            val user =
                !runtime.injector.authApi
                    .login(
                        runtime.injector.environment.studyId(),
                        LoginRequest(PhoneLoginRequest(phone, code))
                    )
                    .async(runtime.fx.M)
                    .attempt()
                    .unwrapUser(runtime)
                    .noneToError(runtime)
                    .mapError(runtime.fx) { error ->

                        if (error is FourYouAndMeError.NetworkErrorHTTP && error.code == 401)
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

            !user.fold({ just(Unit) }, { it.save(runtime) })

            user
        }

    private fun <F> Kind<F, Either<Throwable, Response<UserResponse>>>.unwrapUser(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Option<User>>> =
        runtime.fx.concurrent {

            val request =
                !this@unwrapUser

            val response = request.fold(
                { Throwable().left() },
                { response ->

                    response.unwrapBody(Either.applicativeError())
                        .flatMap { userResponse ->
                            response.headers()[Headers.AUTH]
                                .toOption()
                                .fold({ Throwable().left() }, { userResponse.toUser(it).right() })
                        }

                })

            val configuration =
                ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                    .bind()
                    .toOption()

            !toFourYouAndMeError(runtime, response, configuration.map { it.text })
        }


    /* --- user --- */

    private const val USER_EMAIL = "user_email"
    private const val USER_ID = "user_id"
    private const val USER_TOKEN = "user_token"

    private fun <F> User.save(runtime: Runtime<F>): Kind<F, Unit> =
        runtime.fx.concurrent {

            runtime.injector.prefs
                .edit()
                .putString(USER_EMAIL, email)
                .putString(USER_ID, id.toString())
                .putString(USER_TOKEN, token)
                .apply()

            !runtime.onMainDispatcher { Memory.token = token }

        }

    @Deprecated("use suspend version")
    internal fun <F> loadToken(runtime: Runtime<F>): Kind<F, Option<String>> =
        runtime.fx.concurrent {

            val token =
                runtime.injector.prefs.getString(USER_TOKEN, null).toOption()

            !runtime.onMainDispatcher { Memory.token = token.orNull() }

            token

        }

    internal suspend fun AuthModule.loadToken(): String? {

        val token = prefs.getString(USER_TOKEN, null)

        evalOnMain { Memory.token = token }

        return token

    }
}