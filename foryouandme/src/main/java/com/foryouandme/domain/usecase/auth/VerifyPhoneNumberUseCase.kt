package com.foryouandme.domain.usecase.auth

import com.foryouandme.data.datasource.StudySettings
import javax.inject.Inject

class VerifyPhoneNumberUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val settings: StudySettings
) {

    suspend operator fun invoke(phone: String) {
        repository.verifyPhoneNumber(settings.studyId, phone)
    }

}