package com.foryouandme.data.repository.user

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.user.network.UserApi
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest.Companion.asRequest
import com.foryouandme.domain.usecase.user.UserRepository
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val authErrorInterceptor: AuthErrorInterceptor
): UserRepository {

    override suspend fun getUser(token: String): User? =
        authErrorInterceptor.execute { api.getUser(token) }.toUser(token)

    override suspend fun updateUserTimeZone(token: String, zoneId: ZoneId) {
        authErrorInterceptor.execute { api.updateUserTimeZone(token, zoneId.asRequest()) }
    }

    override suspend fun updateUserCustomData(token: String, data: List<UserCustomData>) {
        authErrorInterceptor.execute { api.updateUserCustomData(token, data.asRequest()) }
    }
}