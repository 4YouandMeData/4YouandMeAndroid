package com.fouryouandme.core.cases.yourdata

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.YourDataModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.yourdata.YourDataRepository.fetchUserDataAttributes
import com.fouryouandme.core.cases.yourdata.YourDataRepository.fetchYourData
import com.fouryouandme.core.entity.yourdata.UserDataAggregation
import com.fouryouandme.core.entity.yourdata.YourData

object YourDataUseCase {

    suspend fun YourDataModule.getYourData(): Either<FourYouAndMeError, YourData?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchYourData(it, environment.studyId()) }

    suspend fun YourDataModule.getUserDataAggregation(
        period: YourDataPeriod
    ): Either<FourYouAndMeError, List<UserDataAggregation>?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchUserDataAttributes(it, environment.studyId(), period) }

}