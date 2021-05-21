package com.foryouandme.domain.usecase.yourdata

import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataPeriod

interface YourDataRepository {

    suspend fun getYourData(token: String, studyId: String): YourData?

    suspend fun getUserDataAggregations(
        period: YourDataPeriod,
        token: String,
        studyId: String
    ): List<UserDataAggregation>

}