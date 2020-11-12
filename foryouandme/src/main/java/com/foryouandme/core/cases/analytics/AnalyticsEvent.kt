package com.foryouandme.core.cases.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.foryouandme.core.cases.yourdata.YourDataPeriod

sealed class AnalyticsEvent(val eventName: String) {

    open fun firebaseBundle(): Bundle = bundleOf()


    /* --- screen events --- */

    sealed class ScreenViewed(eventName: String) : AnalyticsEvent(eventName) {

        object GetStarted : ScreenViewed("GetStarted")
        object SetupLater : ScreenViewed("SetupLater")
        object RequestSetUp : ScreenViewed("RequestAccountSetup")
        object UserRegistration : ScreenViewed("UserRegistration")
        object OtpValidation : ScreenViewed("ValidateOTP")
        object StudyVideo : ScreenViewed("StudyVideo")
        object VideoDiary : ScreenViewed("VideoDiary")
        object AboutYou : ScreenViewed("About You")
        object VideoDiaryComplete : ScreenViewed("VideoDiaryComplete")
        object ConsentName : ScreenViewed("ConsentName")
        object ConsentSignature : ScreenViewed("ConsentSignature")
        object Permissions : ScreenViewed("Permissions")
        object AppsAndDevices : ScreenViewed("AppsAndDevices")
        object EmailInsert : ScreenViewed("Email")
        object EmailVerification : ScreenViewed("EmailVerification")
        object OAuth : ScreenViewed("OAuth")
        object Browser : ScreenViewed("Browser")
        object LearnMore : ScreenViewed("LearnMore")
        object Feed : ScreenViewed("Feed")
        object Task : ScreenViewed("Task")
        object YourData : ScreenViewed("YourData")
        object StudyInfo : ScreenViewed("StudyInfo")
        object PrivacyPolicy : ScreenViewed("PrivacyPolicy")
        object TermsOfService : ScreenViewed("TermsOfService")

    }

    /* --- user --- */

    data class UserRegistration(
        val countryCode: String
    ) : AnalyticsEvent("user_registration") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("account_type", countryCode) }

    }

    /* --- screening --- */

    object CancelDuringScreeningQuestions : AnalyticsEvent("screening_questions_cancelled")

    /* --- informed consent --- */

    data class CancelDuringInformedConsent(
        val pageId: String
    ) : AnalyticsEvent("informed_consent_cancelled") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("page_id", pageId) }

    }

    data class CancelDuringComprehension(
        val pageId: String
    ) : AnalyticsEvent("comprehension_quiz_cancelled") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("question_id", pageId) }

    }

    object ConsentDisagreed : AnalyticsEvent("consent_disagreed")

    object ConsentAgreed : AnalyticsEvent("consent_agreed")

    /* --- main --- */

    sealed class Tab(val name: String) {

        object Feed : Tab("TAB_FEED")
        object Task : Tab("TAB_TASK")
        object UserData : Tab("TAB_USER_DATA")
        object StudyInfo : Tab("TAB_STUDY_INFO")

    }

    data class SwitchTab(val tab: Tab) : AnalyticsEvent("tab_switch") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("tab", tab.name) }
    }

    /* --- quick activity --- */

    data class QuickActivityOptionClicked(
        val quickActivityId: String,
        val optionId: String
    ) : AnalyticsEvent("quick_activity_option_clicked")

    /* --- your data --- */

    data class YourDataSelectDataPeriod(
        val period: YourDataPeriod
    ) : AnalyticsEvent("your_data_period_selection") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply {

                putString(
                    "period",
                    when (period) {
                        YourDataPeriod.Week -> "WEEK"
                        YourDataPeriod.Month -> "MONTH"
                        YourDataPeriod.Year -> "YEAR"
                    }
                )
            }

    }

    /* --- task --- */

    sealed class RecordingAction(val name: String) {

        object Start : RecordingAction("contact")
        object Resume : RecordingAction("start_recording")
        object Pause : RecordingAction("pause_recording")

    }

    data class VideoDiaryAction(
        val action: RecordingAction
    ) : AnalyticsEvent("video_diary_action") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("action", action.name) }

    }

    /* --- permission --- */

    data class LocationPermissionChanged(
        val granted: Boolean
    ) : AnalyticsEvent("location_permission_changed") {

        override fun firebaseBundle(): Bundle =
            Bundle().apply { putString("status", if(granted) "true" else "false") }

    }

}