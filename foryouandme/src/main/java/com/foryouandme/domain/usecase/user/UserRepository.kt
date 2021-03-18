package com.foryouandme.domain.usecase.user

import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId

interface UserRepository {

    suspend fun getUser(token: String): User?

    suspend fun updateUserTimeZone(token: String, zoneId: ZoneId)

    suspend fun updateUserCustomData(token: String, data: List<UserCustomData>)

}