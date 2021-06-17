package com.foryouandme.data.repository.yourdata

import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.yourdata.database.toDatabaseEntity
import com.foryouandme.data.repository.yourdata.network.YourDataApi
import com.foryouandme.domain.usecase.yourdata.YourDataRepository
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataFilter
import com.foryouandme.entity.yourdata.YourDataPeriod
import javax.inject.Inject

class YourDataRepositoryImpl @Inject constructor(
    private val api: YourDataApi,
    private val authErrorInterceptor: AuthErrorInterceptor,
    private val database: ForYouAndMeDatabase
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

    override suspend fun saveFilters(filters: List<YourDataFilter>) {
        database
            .yourDataFilterIdDao()
            .insertYourDataFilterId(*filters.map { it.toDatabaseEntity() }.toTypedArray())
    }

    override suspend fun getFiltersIds(): List<String> =
        database.yourDataFilterIdDao().getYourDataFilterIds().map { it.id }

}