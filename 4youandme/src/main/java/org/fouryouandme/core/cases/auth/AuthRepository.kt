package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.auth.request.LoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneLoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.data.api.auth.response.UserResponse
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.mapNotNull
import retrofit2.Response

object AuthRepository {

    suspend fun AuthModule.verifyPhoneNumber(
        configuration: Configuration,
        phone: String
    ): Either<FourYouAndMeError, Unit> =
        PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
            .pipe { suspend { api.verifyPhoneNumber(environment.studyId(), it) } }
            .pipe { errorModule.unwrapToEither(block = it) }
            .mapLeft {

                if (it is FourYouAndMeError.NetworkErrorHTTP && it.code == 404)
                    FourYouAndMeError.MissingPhoneNumber {
                        configuration.text.phoneVerification.error.errorMissingNumber
                    }
                else it

            }
            .map { Unit }

    suspend fun AuthModule.login(
        configuration: Configuration,
        phone: String,
        code: String,
    ): Either<FourYouAndMeError, User> =
        suspend { api.login(environment.studyId(), LoginRequest(PhoneLoginRequest(phone, code))) }
            .pipe { errorModule.unwrapToEither(block = it) }
            .map { it.unwrapUser() }
            .nullToError()
            .mapLeft { error ->

                if (error is FourYouAndMeError.NetworkErrorHTTP && error.code == 401)
                    FourYouAndMeError.WrongPhoneCode {
                        configuration.text.phoneVerification.error.errorWrongCode
                    }
                else
                    error
            }
            .map { it.also { save(it) } }

    private suspend fun Response<UserResponse>.unwrapUser(): User? =

        mapNotNull(body(), headers()[Headers.AUTH])
            ?.let { (user, token) ->
                user.toUser(token)
            }


/* --- user --- */

    private const val USER_TOKEN = "user_token"

    private suspend fun AuthModule.save(user: User): Unit {
        user.apply {

            prefs.edit().putString(USER_TOKEN, token).apply()

            evalOnMain { Memory.token = token }

        }
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

        val token = prefs.getString(USER_TOKEN, "")

        evalOnMain { Memory.token = token }

        return token

    }

    internal suspend fun AuthModule.fetchUser(token: String): Either<FourYouAndMeError, User?> =
        suspend { api.getUser(token) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.toUser(token) }
}