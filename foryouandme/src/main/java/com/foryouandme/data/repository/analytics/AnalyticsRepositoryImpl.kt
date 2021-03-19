package com.foryouandme.data.repository.analytics

import com.foryouandme.data.datasource.analytics.AnalyticsFirebaseDataSource
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.AnalyticsRepository
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val firebaseDataSource: AnalyticsFirebaseDataSource
) : AnalyticsRepository {

    override suspend fun sendEvent(event: AnalyticsEvent, provider: EAnalyticsProvider) {

        when (provider) {
            EAnalyticsProvider.FIREBASE ->
                firebaseDataSource.sendEvent(event)
            EAnalyticsProvider.ALL ->
                firebaseDataSource.sendEvent(event)
        }

    }

    override suspend fun setUserId(id: String) {
        firebaseDataSource.setUserId(id)
    }
}