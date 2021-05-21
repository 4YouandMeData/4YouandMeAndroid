package com.foryouandme.core.cases.yourdata

import arrow.core.Either
import com.foryouandme.core.arch.deps.modules.YourDataModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataPeriod

object YourDataRepository {

    suspend fun YourDataModule.fetchYourData(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, YourData?> =
        errorModule.unwrapToEither { api.getYourData(token, studyId) }
            .map { it.toYourData() }

    suspend fun YourDataModule.fetchUserDataAttributes(
        token: String,
        studyId: String,
        period: YourDataPeriod
    ): Either<ForYouAndMeError, List<UserDataAggregation>?> =
        errorModule.unwrapToEither { api.getUserDataAggregation(token, studyId, period.value) }
            .map { it.toUserAggregations() }

}