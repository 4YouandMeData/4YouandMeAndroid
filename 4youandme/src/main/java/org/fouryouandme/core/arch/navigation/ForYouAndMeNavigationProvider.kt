package org.fouryouandme.core.arch.navigation

import org.fouryouandme.auth.consent.informed.*
import org.fouryouandme.auth.consent.review.ConsentReviewDisagreeToAuth
import org.fouryouandme.auth.consent.review.ConsentReviewInfoToConsentReviewDisagree
import org.fouryouandme.auth.consent.user.ConsentUserEmailToConsentUserEmailValidationCode
import org.fouryouandme.auth.consent.user.ConsentUserNameToConsentUserEmail
import org.fouryouandme.auth.phone.EnterPhoneToPhoneValidationCode
import org.fouryouandme.auth.phone.code.PhoneValidationCodeToScreening
import org.fouryouandme.auth.screening.*
import org.fouryouandme.auth.signup.info.SignUpInfoToEnterPhone
import org.fouryouandme.auth.signup.info.SignUpInfoToSignUpLater
import org.fouryouandme.auth.splash.SplashToWelcome
import org.fouryouandme.auth.welcome.WelcomeToSignUpInfo
import org.fouryouandme.core.arch.navigation.execution.*

class ForYouAndMeNavigationProvider : NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution =
        when (action) {

            is AnywhereToAuth -> anywhereToAuth()
            is AnywhereToWelcome -> anywhereToWelcome()
            is AnywhereToWeb -> anywhereToWeb(action.url)

            is SplashToWelcome -> splashToWelcome()
            is WelcomeToSignUpInfo -> welcomeToSignUpInfo()
            is SignUpInfoToSignUpLater -> signUpInfoToSignUpLater()
            is SignUpInfoToEnterPhone -> signUpInfoToEnterPhone()
            is EnterPhoneToPhoneValidationCode -> enterPhoneToPhoneValidationCode(
                action.phone,
                action.countryCode
            )
            is PhoneValidationCodeToScreening -> phoneValidationCodeToScreening()

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

            is ConsentUserNameToConsentUserEmail ->
                consentUserNameToConsentUserEmail()
            is ConsentUserEmailToConsentUserEmailValidationCode ->
                consentUserEmailToConsentUserEmailVerificationCode()

            else -> {
                {}
            }
        }
}