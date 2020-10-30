package com.foryouandme.core.cases.yourdata

import arrow.core.Either
import com.foryouandme.core.arch.deps.modules.YourDataModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.yourdata.UserDataAggregation
import com.foryouandme.core.entity.yourdata.YourData

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

sealed class YourDataPeriod(val value: String) {

    object Week : YourDataPeriod("last_week")
    object Month : YourDataPeriod("last_month")
    object Year : YourDataPeriod("last_year")

}