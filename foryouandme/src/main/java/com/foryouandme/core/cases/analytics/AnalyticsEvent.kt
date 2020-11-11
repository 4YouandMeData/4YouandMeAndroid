package com.foryouandme.core.cases.analytics

import android.os.Bundle
import androidx.core.os.bundleOf

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

    object QuickActivityOptionClicked : AnalyticsEvent("quick_activity_option_clicked")

    object StartStudyAction : AnalyticsEvent("study_video_action")

    object ClickFeedTile : AnalyticsEvent("feed_tile_clicked")

    object VideoDiaryAction : AnalyticsEvent("video_diary_action")
    object YourDataSelectDataPeriod : AnalyticsEvent("your_data_period_selection")
    object LocationPermissionChanged : AnalyticsEvent("location_permission_changed")

}