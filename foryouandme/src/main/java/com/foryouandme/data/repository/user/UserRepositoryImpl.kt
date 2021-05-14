package com.foryouandme.data.repository.user

import android.content.SharedPreferences
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.user.network.UserApi
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.request.UserFirebaseTokenDataUpdateRequest
import com.foryouandme.data.repository.user.network.request.UserFirebaseTokenUpdateRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest.Companion.asRequest
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.user.UserRepository
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val authErrorInterceptor: AuthErrorInterceptor,
    private val prefs: SharedPreferences
) : UserRepository {

    // session cache
    private var user: User? = null

    override suspend fun getToken(): String =
        authErrorInterceptor.execute {
            prefs.getString(USER_TOKEN, null) ?: throw ForYouAndMeException.UserNotLoggedIn
        }

    override suspend fun getTokenOrNull(): String? =
        prefs.getString(USER_TOKEN, null)

    override suspend fun getUser(token: String): User? =
        authErrorInterceptor
            .execute { api.getUser(token) }
            .toUser(token)
            .also { user = it }

    override suspend fun loadUser(): User? = user

    override suspend fun saveUser(user: User) {
        prefs.edit().putString(USER_TOKEN, user.token).apply()
    }

    override suspend fun clearUser() {
        prefs.edit().remove(USER_TOKEN).apply()
    }

    override suspend fun updateUserTimeZone(token: String, zoneId: ZoneId) {
        authErrorInterceptor.execute { api.updateUserTimeZone(token, zoneId.asRequest()) }
    }

    override suspend fun updateUserCustomData(token: String, data: List<UserCustomData>) {
        authErrorInterceptor.execute {
            api.updateUserCustomData(token, data.asRequest())
            user = user?.copy(customData = data)
        }
    }

    override suspend fun updateUserFirebaseToken(token: String, firebaseToken: String) {
        authErrorInterceptor.execute {
            api.updateFirebaseToken(
                token,
                UserFirebaseTokenUpdateRequest((UserFirebaseTokenDataUpdateRequest(firebaseToken)))
            )
        }
    }

    companion object {

        // TODO: refactor with room
        private const val USER_TOKEN = "user_token"

    }

}