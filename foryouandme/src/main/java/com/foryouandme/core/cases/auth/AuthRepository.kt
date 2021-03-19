package com.foryouandme.core.cases.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.deps.modules.unwrapResponse
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.Memory
import com.foryouandme.core.cases.push.PushUseCase
import com.foryouandme.data.repository.auth.network.request.*
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.UserResponse
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.PREGNANCY_END_DATE_IDENTIFIER
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataType
import org.threeten.bp.ZoneId
import retrofit2.Response

object AuthRepository {

    suspend fun AuthModule.verifyPhoneNumber(
        configuration: Configuration,
        phone: String
    ): Either<ForYouAndMeError, Unit> =
        PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
            .let { suspend { api.verifyPhoneNumber(environment.studyId(), it) } }
            .let { errorModule.unwrapToEither(it) }
            .let { errorModule.unwrapResponse(it) }
            .mapLeft {

                if (it is ForYouAndMeError.NetworkErrorHTTP && it.code == 404)
                    ForYouAndMeError.MissingPhoneNumber {
                        configuration.text.phoneVerification.error.errorMissingNumber
                    }
                else it

            }
            .map { Unit }

    suspend fun AuthModule.login(
        configuration: Configuration,
        phone: String,
        code: String,
    ): Either<ForYouAndMeError, User> =
        suspend { api.login(environment.studyId(), LoginRequest(PhoneLoginRequest(phone, code))) }
            .let { errorModule.unwrapToEither(block = it) }
            .map { it.unwrapUser() }
            .nullToError()
            .mapLeft { error ->

                if (error is ForYouAndMeError.NetworkErrorHTTP && error.code == 401)
                    ForYouAndMeError.WrongPhoneCode {
                        configuration.text.phoneVerification.error.errorWrongCode
                    }
                else
                    error
            }
            .flatMap { user ->
                // update timezone
                updateUserTimeZone(user.token, ZoneId.systemDefault())
                    .map { user }
            }
            .flatMap {user ->

                PushUseCase.getPushToken()
                    .map { updateFirebaseToken(user.token, it) }
                    .map { user }

            }
            .map { it.also { save(it) } }
            .map { it.also { Memory.user = it } }

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

    internal suspend fun AuthModule.loadToken(): String? {

        val token = prefs.getString(USER_TOKEN, null)

        evalOnMain { Memory.token = token }

        return token

    }

    internal suspend fun AuthModule.fetchUser(token: String): Either<ForYouAndMeError, User?> =
        suspend { api.getUser(token) }
            .let { errorModule.unwrapToEither(it) }
            .flatMap {

                // if user has empty custom data update it with default configuration
                if (environment.useCustomData() && (it.customData == null || it.customData.isEmpty()))

                    updateUserCustomData(token, defaultUserCustomData())
                        .flatMap { fetchUser(token) }
                else
                    it.toUser(token).right()

            }
            .map { it.also { Memory.user = it } }

    // TODO: remove this and handle default configuration from server
    private fun defaultUserCustomData(): List<UserCustomData> =
        listOf(
            UserCustomData(
                identifier = PREGNANCY_END_DATE_IDENTIFIER,
                type = UserCustomDataType.Date,
                name = "Your due date",
                value = null
            )
        )

    internal suspend fun AuthModule.updateUserCustomData(
        token: String,
        data: List<UserCustomData>
    ): Either<ForYouAndMeError, Unit> =
        suspend { api.updateUserCustomData(token, data.asRequest()) }
            .let { errorModule.unwrapToEither(it) }

    internal suspend fun AuthModule.updateUserTimeZone(
        token: String,
        timeZone: ZoneId
    ): Either<ForYouAndMeError, Unit> =
        suspend { api.updateUserTimeZone(token, timeZone.asRequest()) }
            .let { errorModule.unwrapToEither(it) }

    internal suspend fun AuthModule.updateFirebaseToken(
        token: String,
        firebaseToken: String
    ): Either<ForYouAndMeError, Unit> =
        UserFirebaseTokenUpdateRequest(UserFirebaseTokenDataUpdateRequest(firebaseToken))
            .let { suspend { api.updateFirebaseToken(token, it) } }
            .let { errorModule.unwrapToEither(it) }

}