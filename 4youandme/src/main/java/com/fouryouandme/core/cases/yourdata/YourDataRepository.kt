package com.fouryouandme.core.cases.yourdata

import arrow.core.Either
import com.fouryouandme.core.arch.deps.modules.YourDataModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.yourdata.UserDataAggregation
import com.fouryouandme.core.entity.yourdata.YourData

object YourDataRepository {

    suspend fun YourDataModule.fetchYourData(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, YourData?> =
        errorModule.unwrapToEither { api.getYourData(token, studyId) }
            .map { it.toYourData() }

    suspend fun YourDataModule.fetchUserDataAttributes(
        token: String,
        studyId: String,
        period: YourDataPeriod
    ): Either<FourYouAndMeError, List<UserDataAggregation>?> =
        errorModule.unwrapToEither { api.getUserDataAggregation(token, studyId, period.value) }
            .map { it.toUserAggregations() }

}

sealed class YourDataPeriod(val value: String) {

    object Week : YourDataPeriod("last_week")
    object Month : YourDataPeriod("last_month")
    object Year : YourDataPeriod("last_year")

}