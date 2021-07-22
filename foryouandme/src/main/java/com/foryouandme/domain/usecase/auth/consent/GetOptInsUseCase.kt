package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.optins.OptIns
import javax.inject.Inject

class GetOptInsUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(): OptIns? =
        repository.getOptIns(getTokenUseCase(), settings.studyId)

}