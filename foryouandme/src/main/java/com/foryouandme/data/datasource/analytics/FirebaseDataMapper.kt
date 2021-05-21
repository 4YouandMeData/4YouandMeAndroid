package com.foryouandme.data.datasource.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.entity.yourdata.YourDataPeriod
import javax.inject.Inject

class FirebaseDataMapper @Inject constructor() {

    fun map(source: AnalyticsEvent): Bundle =
        when (source) {

            is AnalyticsEvent.UserRegistration ->
                bundleOf("account_type" to source.countryCode)
            is AnalyticsEvent.CancelDuringInformedConsent ->
                bundleOf("page_id" to source.pageId)
            is AnalyticsEvent.CancelDuringComprehension ->
                bundleOf("question_id" to source.pageId)
            is AnalyticsEvent.SwitchTab ->
                bundleOf("tab" to source.tab.name)
            is AnalyticsEvent.YourDataSelectDataPeriod ->
                bundleOf(
                    "period" to
                            when (source.period) {
                                YourDataPeriod.Week -> "WEEK"
                                YourDataPeriod.Month -> "MONTH"
                                YourDataPeriod.Year -> "YEAR"
                            }
                )
            is AnalyticsEvent.VideoDiaryAction ->
                bundleOf("action" to source.action.name)
            is AnalyticsEvent.LocationPermissionChanged ->
                bundleOf("status" to if (source.granted) "true" else "false")
            else -> bundleOf()
        }

}