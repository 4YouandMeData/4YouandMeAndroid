package com.foryouandme.domain.usecase.auth

import com.foryouandme.data.datasource.Environment
import javax.inject.Inject

class VerifyPhoneNumberUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val environment: Environment
) {

    suspend operator fun invoke(phone: String) {
        repository.verifyPhoneNumber(environment.studyId(), phone)
    }

}