package com.foryouandme.domain.usecase.auth

import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData

interface AuthRepository {

    suspend fun getToken(): String

    suspend fun getTokenOrNull(): String?

    suspend fun login(phone: String, code: String, countryCode: String): User

    suspend fun getUser(token: String): User?

    suspend fun updateUserCustomData(
        token: String,
        data: List<UserCustomData>
    )

}