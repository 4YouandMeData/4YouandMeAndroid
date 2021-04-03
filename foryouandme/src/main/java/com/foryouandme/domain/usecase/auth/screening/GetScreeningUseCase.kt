package com.foryouandme.domain.usecase.auth.screening

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.screening.Screening
import javax.inject.Inject

class GetScreeningUseCase @Inject constructor(
    private val repository: ScreeningRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): Screening? =
        repository.getScreening(getTokenUseCase(), environment.studyId())

}