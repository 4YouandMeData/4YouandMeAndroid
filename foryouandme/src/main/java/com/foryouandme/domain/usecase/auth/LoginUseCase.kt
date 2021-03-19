package com.foryouandme.domain.usecase.auth

import com.foryouandme.entity.user.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(phone: String, code: String, countryCode: String): User =
        repository.login(phone, code, countryCode)

}