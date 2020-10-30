package com.foryouandme.core.cases.analytics

import com.foryouandme.core.arch.deps.modules.AnalyticsModule

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
        firebase.logEvent(event.eventName, event.firebaseBundle())

}