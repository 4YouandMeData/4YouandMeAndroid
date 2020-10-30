package com.foryouandme.core.cases.yourdata

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.YourDataModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.yourdata.YourDataRepository.fetchUserDataAttributes
import com.foryouandme.core.cases.yourdata.YourDataRepository.fetchYourData
import com.foryouandme.core.entity.yourdata.UserDataAggregation
import com.foryouandme.core.entity.yourdata.YourData

object YourDataUseCase {

    suspend fun YourDataModule.getYourData(): Either<ForYouAndMeError, YourData?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchYourData(it, environment.studyId()) }

    suspend fun YourDataModule.getUserDataAggregation(
        period: YourDataPeriod
    ): Either<ForYouAndMeError, List<UserDataAggregation>?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchUserDataAttributes(it, environment.studyId(), period) }

}