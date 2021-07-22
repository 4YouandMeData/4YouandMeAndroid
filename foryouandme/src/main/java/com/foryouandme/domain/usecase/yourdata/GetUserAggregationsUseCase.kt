package com.foryouandme.domain.usecase.yourdata

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourDataPeriod
import javax.inject.Inject

class GetUserAggregationsUseCase @Inject constructor(
    private val repository: YourDataRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(period: YourDataPeriod): List<UserDataAggregation> =
        repository.getUserDataAggregations(
            period,
            getTokenUseCase(),
            settings.studyId
        )

}