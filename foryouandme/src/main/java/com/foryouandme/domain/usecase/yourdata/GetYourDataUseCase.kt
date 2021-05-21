package com.foryouandme.domain.usecase.yourdata

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.yourdata.YourData
import javax.inject.Inject

class GetYourDataUseCase @Inject constructor(
    private val repository: YourDataRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): YourData? =
        repository.getYourData(
            getTokenUseCase(),
            environment.studyId()
        )

}