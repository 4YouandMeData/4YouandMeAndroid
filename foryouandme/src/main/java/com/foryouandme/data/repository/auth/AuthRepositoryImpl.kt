package com.foryouandme.data.repository.auth

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.data.repository.auth.network.request.*
import com.foryouandme.data.repository.user.network.response.UserResponse
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.auth.AuthRepository
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun phoneLogin(
        studyId: String,
        phone: String,
        code: String,
        configuration: Configuration
    ): User? =
        catchLoginError(ForYouAndMeException.WrongCode()) {
            api.phoneLogin(studyId, LoginRequest(PhoneLoginRequest(phone, code)))
                .unwrapUser(configuration)
        }

    override suspend fun pinLogin(
        studyId: String,
        pin: String,
        configuration: Configuration
    ): User? =
        catchLoginError(ForYouAndMeException.WrongCode()) {
            api.pinLogin(studyId, LoginRequest(PinLoginRequest(pin))).unwrapUser(configuration)
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

    private fun Response<UserResponse>.unwrapUser(configuration: Configuration): User? =
        if (isSuccessful) {
            val user = body()
            val token = headers()[Headers.AUTH]
            if (user != null && token != null) user.toUser(token, configuration) else null
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