package com.foryouandme.domain.usecase.auth

import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId

interface AuthRepository {

    suspend fun phoneLogin(studyId: String, phone: String, code: String): User?

    suspend fun pinLogin(studyId: String, pin: String): User?

    suspend fun verifyPhoneNumber(studyId: String, phone: String)

}