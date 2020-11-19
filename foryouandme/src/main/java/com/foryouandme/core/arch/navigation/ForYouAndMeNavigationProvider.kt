package com.foryouandme.core.arch.navigation

import com.foryouandme.aboutyou.*
import com.foryouandme.auth.consent.informed.*
import com.foryouandme.auth.consent.review.ConsentReviewDisagreeToAuth
import com.foryouandme.auth.consent.review.ConsentReviewInfoToConsentReviewDisagree
import com.foryouandme.auth.consent.review.ConsentReviewToOptIns
import com.foryouandme.auth.consent.user.*
import com.foryouandme.auth.integration.*
import com.foryouandme.auth.optin.OptInPermissionToOptInPermission
import com.foryouandme.auth.optin.OptInPermissionToOptInSuccess
import com.foryouandme.auth.optin.OptInToConsentUser
import com.foryouandme.auth.optin.OptInWelcomeToOptInPermission
import com.foryouandme.auth.phone.EnterPhoneToPhoneValidationCode
import com.foryouandme.auth.phone.code.PhoneValidationCodeToMain
import com.foryouandme.auth.phone.code.PhoneValidationCodeToVideo
import com.foryouandme.auth.screening.*
import com.foryouandme.auth.signup.info.SignUpInfoToEnterPhone
import com.foryouandme.auth.signup.info.SignUpInfoToSignUpLater
import com.foryouandme.auth.splash.SplashToMain
import com.foryouandme.auth.splash.SplashToVideo
import com.foryouandme.auth.splash.SplashToWelcome
import com.foryouandme.auth.video.VideoToScreening
import com.foryouandme.auth.welcome.WelcomeToSignUpInfo
import com.foryouandme.core.arch.navigation.execution.*
import com.foryouandme.main.MainPageToAboutYouPage
import com.foryouandme.main.MainPageToHtmlDetailsPage
import com.foryouandme.main.MainToTask
import com.foryouandme.main.feeds.FeedsToTask
import com.foryouandme.main.tasks.TasksToTask
import com.foryouandme.tasks.StepToStep

class ForYouAndMeNavigationProvider : NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution =
        when (action) {

            is AnywhereToAuth -> anywhereToAuth()
            is AnywhereToWelcome -> anywhereToWelcome()
            is AnywhereToWeb -> anywhereToWeb(action.url)

            is SplashToWelcome -> splashToWelcome()
            is SplashToVideo -> splashToVideo()
            is SplashToMain -> splashToMain()

            is WelcomeToSignUpInfo -> welcomeToSignUpInfo()

            is SignUpInfoToSignUpLater -> signUpInfoToSignUpLater()
            is SignUpInfoToEnterPhone -> signUpInfoToEnterPhone()

            is EnterPhoneToPhoneValidationCode ->
                enterPhoneToPhoneValidationCode(
                    action.phone,
                    action.countryCode
                )

            is PhoneValidationCodeToVideo -> phoneValidationCodeToScreening()
            is PhoneValidationCodeToMain -> phoneValidationCodeToMain()

            is VideoToScreening -> videoToScreening()

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
                tasksToTask(action.id)

            is FeedsToTask ->
                feedsToTask(action.id)

            is StepToStep -> stepToStep(action.index)

            is MainPageToAboutYouPage ->
                mainPageToAboutYouPage()
            is MainPageToHtmlDetailsPage ->
                mainPageToHtmlDetailsPage(action.pageId)
            is MainToTask ->
                mainToTask(action.id)

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