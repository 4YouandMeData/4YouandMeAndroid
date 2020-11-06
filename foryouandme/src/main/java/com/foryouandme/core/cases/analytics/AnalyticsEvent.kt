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

}