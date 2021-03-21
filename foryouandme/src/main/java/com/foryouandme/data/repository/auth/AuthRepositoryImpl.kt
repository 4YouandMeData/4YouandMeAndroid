package com.foryouandme.data.repository.auth

import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.data.repository.auth.network.request.*
import com.foryouandme.data.repository.user.network.UserResponse
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.auth.AuthRepository
import com.foryouandme.entity.user.User
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun phoneLogin(studyId: String, phone: String, code: String): User? =
        catchLoginError(ForYouAndMeException.WrongCode()) {
            api.phoneLogin(studyId, LoginRequest(PhoneLoginRequest(phone, code))).unwrapUser()
        }

    override suspend fun pinLogin(studyId: String, pin: String): User? =
        catchLoginError(ForYouAndMeException.WrongCode()) {
            api.pinLogin(studyId, LoginRequest(PinLoginRequest(pin))).unwrapUser()
        }

    private suspend fun <T> catchLoginError(
        accessError: ForYouAndMeException,
        block: suspend () -> T
    ): T =
        try {
            block()
        } catch (throwable: Throwable) {

            if (throwable is HttpException &&
                (throwable.code() == 401 || throwable.code() == 404)
            )
                throw accessError
            else
                throw throwable

        }

    private fun Response<UserResponse>.unwrapUser(): User? =
        if (isSuccessful) {
            mapNotNull(body(), headers()[Headers.AUTH])
                ?.let { (user, token) -> user.toUser(token) }
        } else
            throw HttpException(this)

    override suspend fun verifyPhoneNumber(studyId: String, phone: String) {
        catchLoginError(ForYouAndMeException.MissingPhoneNumber()) {
            api.verifyPhoneNumber(
                studyId,
                PhoneNumberVerificationRequest(PhoneNumberRequest(phone))
            ).unwrapResponse()
        }
    }

    private fun <T> Response<T>.unwrapResponse(): T? =
        if (isSuccessful) body()
        else throw HttpException(this)

}