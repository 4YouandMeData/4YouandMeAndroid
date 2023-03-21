package com.foryouandme.domain.usecase.user

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId

interface UserRepository {

    suspend fun getToken(): String

    suspend fun getTokenOrNull(): String?

    suspend fun getUser(token: String, configuration: Configuration): User?

    suspend fun loadUser(): User?

    suspend fun saveUser(user: User)

    suspend fun clearUser()

    suspend fun updateUserTimeZone(token: String, zoneId: ZoneId)

    suspend fun updateUserCustomData(
        token: String,
        data: List<UserCustomData>,
        configuration: Configuration
    )

    suspend fun updateUserFirebaseToken(token: String, firebaseToken: String)

}