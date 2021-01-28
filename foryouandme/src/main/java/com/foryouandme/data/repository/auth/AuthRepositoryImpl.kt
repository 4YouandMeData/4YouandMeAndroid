package com.foryouandme.data.repository.auth

import android.content.SharedPreferences
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val authErrorInterceptor: AuthErrorInterceptor
) : AuthRepository {

    override suspend fun getToken(): String =
        authErrorInterceptor.execute {
            prefs.getString(USER_TOKEN, null) ?: throw ForYouAndMeException.UserNotLoggedIn
        }

    companion object {

        private const val USER_TOKEN = "user_token"

    }

}