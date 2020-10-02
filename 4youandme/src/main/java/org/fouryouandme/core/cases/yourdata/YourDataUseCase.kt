package org.fouryouandme.core.cases.yourdata

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.YourDataModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.yourdata.YourDataRepository.fetchUserDataAttributes
import org.fouryouandme.core.cases.yourdata.YourDataRepository.fetchYourData
import org.fouryouandme.core.entity.yourdata.UserDataAggregation
import org.fouryouandme.core.entity.yourdata.YourData

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