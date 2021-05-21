package com.foryouandme.data.repository.yourdata

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.yourdata.network.YourDataApi
import com.foryouandme.domain.usecase.yourdata.YourDataRepository
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataPeriod
import javax.inject.Inject

class YourDataRepositoryImpl @Inject constructor(
    private val api: YourDataApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : YourDataRepository {

    override suspend fun getYourData(token: String, studyId: String): YourData? =
        authErrorInterceptor.execute { api.getYourData(token, studyId) }.toYourData()

    override suspend fun getUserDataAggregations(
        period: YourDataPeriod,
        token: String,
        studyId: String
    ): List<UserDataAggregation> =
        authErrorInterceptor.execute { api.getUserDataAggregation(token, studyId, period.value) }
            .toUserAggregations() ?: emptyList()

}