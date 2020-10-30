package com.fouryouandme.core.cases.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.AuthModule
import com.fouryouandme.core.arch.deps.modules.nullToError
import com.fouryouandme.core.arch.deps.modules.unwrapResponse
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.Memory
import com.fouryouandme.core.data.api.Headers
import com.fouryouandme.core.data.api.auth.request.LoginRequest
import com.fouryouandme.core.data.api.auth.request.PhoneLoginRequest
import com.fouryouandme.core.data.api.auth.request.PhoneNumberRequest
import com.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import com.fouryouandme.core.data.api.auth.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.fouryouandme.core.data.api.auth.request.UserTimeZoneUpdateRequest.Companion.asRequest
import com.fouryouandme.core.data.api.auth.response.UserResponse
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.user.*
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.mapNotNull
import org.threeten.bp.ZoneId
import retrofit2.Response

object AuthRepository {

    suspend fun AuthModule.verifyPhoneNumber(
        configuration: Configuration,
        phone: String
    ): Either<FourYouAndMeError, Unit> =
        PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
            .pipe { suspend { api.verifyPhoneNumber(environment.studyId(), it) } }
            .pipe { errorModule.unwrapToEither(it) }
            .pipe { errorModule.unwrapResponse(it) }
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

        val token = prefs.getString(USER_TOKEN, "")

        evalOnMain { Memory.token = token }

        return token

    }

    internal suspend fun AuthModule.fetchUser(token: String): Either<FourYouAndMeError, User?> =
        suspend { api.getUser(token) }
            .pipe { errorModule.unwrapToEither(it) }
            .flatMap {

                // if user has empty custom data update it with default configuration
                if (it.customData == null || it.customData.isEmpty())
                    updateUserCustomData(token, defaultUserCustomData())
                        .flatMap { fetchUser(token) }
                else if (it.timeZone != ZoneId.systemDefault().id)
                    updateUserTimeZone(token, ZoneId.systemDefault())
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
            ),
            UserCustomData(
                identifier = BABY_GENDER_IDENTIFIER,
                type =
                UserCustomDataType.Items(
                    listOf(
                        UserCustomDataItem(identifier = "1", value = "It's a Boy!"),
                        UserCustomDataItem(identifier = "2", value = "It's a Girl!"),
                    )
                ),
                name = "Your baby's gender",
                value = null
            ),
            UserCustomData(
                identifier = BABY_NAME_IDENTIFIER,
                type = UserCustomDataType.String,
                name = "Your baby's name",
                value = null
            ),
        )

    internal suspend fun AuthModule.updateUserCustomData(
        token: String,
        data: List<UserCustomData>
    ): Either<FourYouAndMeError, Unit> =
        suspend { api.updateUserCustomData(token, data.asRequest()) }
            .pipe { errorModule.unwrapToEither(it) }

    private suspend fun AuthModule.updateUserTimeZone(
        token: String,
        timeZone: ZoneId
    ): Either<FourYouAndMeError, Unit> =
        suspend { api.updateUserTimeZone(token, timeZone.asRequest()) }
            .pipe { errorModule.unwrapToEither(it) }

}