package com.foryouandme.core.cases.analytics

import com.foryouandme.core.arch.deps.modules.AnalyticsModule
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
            else -> firebase.logEvent(event.eventName, event.firebaseBundle())
        }

}