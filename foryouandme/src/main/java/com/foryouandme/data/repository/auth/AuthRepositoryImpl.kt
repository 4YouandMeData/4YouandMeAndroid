package com.foryouandme.data.repository.auth

import android.content.SharedPreferences
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.data.repository.auth.network.request.LoginRequest
import com.foryouandme.data.repository.auth.network.request.PhoneLoginRequest
import com.foryouandme.data.repository.auth.network.request.PinLoginRequest
import com.foryouandme.data.repository.user.network.UserResponse
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest.Companion.asRequest
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.auth.AuthRepository
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val prefs: SharedPreferences,
    private val authErrorInterceptor: AuthErrorInterceptor
) : AuthRepository {

    override suspend fun phoneLogin(studyId: String, phone: String, code: String): User? =
        catchLoginError {
            api.phoneLogin(studyId, LoginRequest(PhoneLoginRequest(phone, code))).unwrapUser()
        }

    override suspend fun pinLogin(studyId: String, pin: String): User? =
        catchLoginError {
            api.pinLogin(studyId, LoginRequest(PinLoginRequest(pin))).unwrapUser()
        }

    private suspend fun catchLoginError(block: suspend () -> User?): User? =
        try {
            block()
        } catch (throwable: Throwable) {

            if (throwable is HttpException && throwable.code() == 401)
                throw ForYouAndMeException.WrongCode()
            else
                throw throwable

        }

    private fun Response<UserResponse>.unwrapUser(): User? =
        mapNotNull(body(), headers()[Headers.AUTH])
            ?.let { (user, token) -> user.toUser(token) }

}