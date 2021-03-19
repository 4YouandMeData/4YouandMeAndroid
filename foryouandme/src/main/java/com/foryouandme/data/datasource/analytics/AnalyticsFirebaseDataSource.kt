package com.foryouandme.data.datasource.analytics

import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import javax.inject.Inject

class AnalyticsFirebaseDataSource @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseDataMapper: FirebaseDataMapper
) {

    fun sendEvent(event: AnalyticsEvent) {

        when (event) {
            is AnalyticsEvent.ScreenViewed ->
                firebaseAnalytics
                    .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                        param(FirebaseAnalytics.Param.SCREEN_NAME, event.eventName)
                    }
            else ->
                firebaseAnalytics.logEvent(event.eventName, firebaseDataMapper.map(event))

        }

    }

    fun setUserId(id: String) {
        firebaseAnalytics.setUserId(id)
    }

}