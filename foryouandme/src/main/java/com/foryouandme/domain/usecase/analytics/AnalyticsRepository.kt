package com.foryouandme.domain.usecase.analytics

interface AnalyticsRepository {

    suspend fun sendEvent(event: AnalyticsEvent, provider: EAnalyticsProvider)

    suspend fun setUserId(id: String)

}