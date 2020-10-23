package org.fouryouandme.core.cases.analytics

import android.os.Bundle
import androidx.core.os.bundleOf

sealed class AnalyticsEvent(val eventName: String) {

    open fun firebaseBundle(): Bundle = bundleOf()

}