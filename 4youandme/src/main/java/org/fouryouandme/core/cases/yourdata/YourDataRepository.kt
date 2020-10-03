package org.fouryouandme.core.cases.yourdata

import arrow.core.Either
import org.fouryouandme.core.arch.deps.modules.YourDataModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.yourdata.UserDataAggregation
import org.fouryouandme.core.entity.yourdata.YourData

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

    object Day : YourDataPeriod("last_day")
    object Week : YourDataPeriod("last_week")
    object Month : YourDataPeriod("last_month")
    object Year : YourDataPeriod("last_year")

}