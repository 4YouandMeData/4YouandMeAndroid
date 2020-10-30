package com.foryouandme.core.cases.analytics

import android.os.Bundle
import androidx.core.os.bundleOf

sealed class AnalyticsEvent(val eventName: String) {

    open fun firebaseBundle(): Bundle = bundleOf()


    /*data class ExampleEventWithParams(val id: String): AnalyticsEvent("example_name") {

        override fun firebaseBundle(): Bundle =
            Bundle().also { it.putString("id", id) }

    }

    object ExampleEvent: AnalyticsEvent("example_name_2")*/

}