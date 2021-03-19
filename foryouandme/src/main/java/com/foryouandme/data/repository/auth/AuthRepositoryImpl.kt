package com.foryouandme.data.repository.auth

import android.content.SharedPreferences
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.auth.AuthRepository
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val prefs: SharedPreferences,
    private val authErrorInterceptor: AuthErrorInterceptor
) : AuthRepository {

    override suspend fun getToken(): String =
        authErrorInterceptor.execute {
            prefs.getString(USER_TOKEN, null) ?: throw ForYouAndMeException.UserNotLoggedIn
        }

    override suspend fun getTokenOrNull(): String? =
        prefs.getString(USER_TOKEN, null)

    override suspend fun login(phone: String, code: String, countryCode: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(token: String): User? =
        authErrorInterceptor.execute { api.getUser(token) }
            .toUser(token)

    override suspend fun updateUserCustomData(
        token: String,
        data: List<UserCustomData>
    ) {
        authErrorInterceptor.execute { api.updateUserCustomData(token, data.asRequest()) }
    }

    companion object {

        private const val USER_TOKEN = "user_token"

    }

}