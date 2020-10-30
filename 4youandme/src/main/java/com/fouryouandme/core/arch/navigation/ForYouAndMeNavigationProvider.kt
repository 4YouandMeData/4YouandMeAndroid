package com.fouryouandme.core.arch.navigation

import com.fouryouandme.aboutyou.*
import com.fouryouandme.auth.consent.informed.*
import com.fouryouandme.auth.consent.review.ConsentReviewDisagreeToAuth
import com.fouryouandme.auth.consent.review.ConsentReviewInfoToConsentReviewDisagree
import com.fouryouandme.auth.consent.review.ConsentReviewToOptIns
import com.fouryouandme.auth.consent.user.*
import com.fouryouandme.auth.integration.*
import com.fouryouandme.auth.optin.OptInPermissionToOptInPermission
import com.fouryouandme.auth.optin.OptInPermissionToOptInSuccess
import com.fouryouandme.auth.optin.OptInToConsentUser
import com.fouryouandme.auth.optin.OptInWelcomeToOptInPermission
import com.fouryouandme.auth.phone.EnterPhoneToPhoneValidationCode
import com.fouryouandme.auth.phone.code.PhoneValidationCodeToMain
import com.fouryouandme.auth.phone.code.PhoneValidationCodeToScreening
import com.fouryouandme.auth.screening.*
import com.fouryouandme.auth.signup.info.SignUpInfoToEnterPhone
import com.fouryouandme.auth.signup.info.SignUpInfoToSignUpLater
import com.fouryouandme.auth.splash.SplashToMain
import com.fouryouandme.auth.splash.SplashToWelcome
import com.fouryouandme.auth.welcome.WelcomeToSignUpInfo
import com.fouryouandme.core.arch.navigation.execution.*
import com.fouryouandme.main.MainPageToAboutYouPage
import com.fouryouandme.main.MainPageToHtmlDetailsPage
import com.fouryouandme.main.tasks.TasksToTask
import com.fouryouandme.tasks.StepToStep

class ForYouAndMeNavigationProvider : NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution =
        when (action) {

            is AnywhereToAuth -> anywhereToAuth()
            is AnywhereToWelcome -> anywhereToWelcome()
            is AnywhereToWeb -> anywhereToWeb(action.url)

            is SplashToWelcome -> splashToWelcome()
            is SplashToMain -> splashToMain()
            is WelcomeToSignUpInfo -> welcomeToSignUpInfo()
            is SignUpInfoToSignUpLater -> signUpInfoToSignUpLater()
            is SignUpInfoToEnterPhone -> signUpInfoToEnterPhone()
            is EnterPhoneToPhoneValidationCode -> enterPhoneToPhoneValidationCode(
                action.phone,
                action.countryCode
            )
            is PhoneValidationCodeToScreening -> phoneValidationCodeToScreening()
            is PhoneValidationCodeToMain -> phoneValidationCodeToMain()

            is ScreeningWelcomeToScreeningQuestions -> screeningWelcomeToScreeningQuestions()
            is ScreeningWelcomeToScreeningPage -> screeningWelcomeToScreeningPage(action.id)
            is ScreeningPageToScreeningPage -> screeningPageToScreeningPage(action.id)
            is ScreeningPageToScreeningQuestions -> screeningPageToScreeningQuestions()
            is ScreeningQuestionsToScreeningSuccess -> screeningQuestionsToScreeningSuccess()
            is ScreeningQuestionsToScreeningFailure -> screeningQuestionsToScreeningFailure()
            is ScreeningFailureToScreeningWelcome -> screeningFailureToScreeningWelcome()
            is ScreeningToConsentInfo -> screeningToConsentInfo()

            is ConsentInfoWelcomeToConsentInfoPage ->
                consentInfoWelcomeToConsentInfoPage(action.id)
            is ConsentInfoWelcomeToConsentInfoQuestion ->
                consentInfoWelcomeToConsentInfoQuestion(action.index)
            is ConsentInfoPageToConsentInfoPage ->
                consentInfoPageToConsentInfoPage(action.id)
            is ConsentInfoPageToConsentInfoModalPage ->
                consentInfoPageToConsentInfoModalPage(action.id)
            is ConsentInfoPageToConsentInfoQuestion ->
                consentInfoPageToConsentInfoQuestion(action.index)
            is ConsentInfoQuestionToConsentInfoQuestion ->
                consentInfoQuestionToConsentInfoQuestion(action.index)
            is ConsentInfoQuestionToConsentInfoSuccess ->
                consentInfoQuestionToConsentInfoSuccess()
            is ConsentInfoQuestionToConsentInfoFailure ->
                consentInfoQuestionToConsentInfoFailure()
            is ConsentInfoFailureToConsentInfoWelcome ->
                consentInfoFailureToConsentInfoWelcome()
            is ConsentInfoFailureToConsentInfoPage ->
                consentInfoFailureToConsentInfoPage(action.id)
            is ConsentInfoToConsentReview ->
                consentInfoToConsentReview()

            is ConsentReviewInfoToConsentReviewDisagree ->
                consentReviewInfoToConsentReviewDisagree()
            is ConsentReviewDisagreeToAuth ->
                consentReviewDisagreeToAuth()
            is ConsentReviewToOptIns ->
                consentReviewToOptIns()

            is OptInWelcomeToOptInPermission ->
                optInWelcomeToOptInPermission(action.index)
            is OptInPermissionToOptInPermission ->
                optInPermissionToOptInPermission(action.index)
            is OptInPermissionToOptInSuccess ->
                optInPermissionToOptInSuccess()
            is OptInToConsentUser ->
                optInToConsentUser()


            is ConsentUserNameToConsentUserEmail ->
                consentUserNameToConsentUserEmail()
            is ConsentUserEmailToConsentUserEmailValidationCode ->
                consentUserEmailToConsentUserEmailValidationCode()
            is ConsentUserEmailValidationCodeToConsentUserSignature ->
                consentUserEmailValidationCodeToConsentUserSignature()
            is ConsentUserSignatureToConsentUserSuccess ->
                consentUserSignatureToConsentUserSuccess()
            is ConsentUserToIntegration ->
                consentUserToIntegration()

            is IntegrationWelcomeToIntegrationPage ->
                integrationWelcomeToIntegrationPage(action.pageId)
            is IntegrationWelcomeToIntegrationLogin ->
                integrationWelcomeToIntegrationLogin(action.url, action.nextPage)
            is IntegrationWelcomeToIntegrationSuccess ->
                integrationWelcomeToIntegrationSuccess()
            is IntegrationPageToIntegrationPage ->
                integrationPageToIntegrationPage(action.pageId)
            is IntegrationPageToIntegrationLogin ->
                integrationPageToIntegrationLogin(action.url, action.nextPage)
            is IntegrationPageToIntegrationSuccess ->
                integrationPageToIntegrationSuccess()
            is IntegrationLoginToIntegrationPage ->
                integrationLoginToIntegrationPage(action.id)
            is IntegrationLoginToIntegrationSuccess ->
                integrationLoginToIntegrationSuccess()
            is IntegrationSuccessToMain ->
                integrationSuccessToMain()

            is TasksToTask ->
                tasksToTask(action.type, action.id)

            is StepToStep -> stepToStep(action.index)

            is MainPageToAboutYouPage ->
                mainPageToAboutYouPage()
            is MainPageToHtmlDetailsPage ->
                mainPageToHtmlDetailsPage(action.pageId)

            is AboutYouMenuPageToAboutYouReviewConsentPage ->
                aboutYouMenuPageToAboutYouReviewConsentPage()
            is AboutYouMenuPageToAppsAndDevicesPage ->
                aboutYouMenuPageToAboutYouAppsAndDevicesPage()
            is AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin ->
                aboutYouDataAppsAndDevicesToAboutYouIntegrationLogin(action.url)
            is AboutYouMenuPageToPermissionsPage ->
                aboutYouMenuPageToPermissionsPage()
            is AboutYouMenuPageToUserInfoPage ->
                aboutYouMenuPageToUserInfoPage()

            else -> {
                {}
            }
        }
}