package com.foryouandme.core.cases.analytics

import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.data.datasource.analytics.FirebaseDataMapper
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

object AnalyticsUseCase {

    suspend fun AnalyticsModule.logEvent(
        event: AnalyticsEvent,
        provider: EAnalyticsProvider
    ): Unit {
        when (provider) {
            EAnalyticsProvider.FIREBASE -> logFirebaseEvent(event)
            EAnalyticsProvider.ALL ->
                logFirebaseEvent(event)
        }
    }

    private suspend fun AnalyticsModule.logFirebaseEvent(event: AnalyticsEvent): Unit =
        when (event) {
            is AnalyticsEvent.ScreenViewed ->
                firebase.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, event.eventName)
                }
            else -> firebase.logEvent(event.eventName, FirebaseDataMapper().map(event))
        }

    suspend fun AnalyticsModule.setUserId(userId: String): Unit =
        firebase.setUserId(userId)

    suspend fun AnalyticsModule.setUserProperty(property: UserProperty, value: String): Unit =
        firebase.setUserProperty(property.name, value)

}