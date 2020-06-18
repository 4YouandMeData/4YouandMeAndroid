package org.fouryouandme.core.arch.navigation

import org.fouryouandme.auth.consent.*
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
            is ScreeningToConsent -> screeningToConsent()

            is ConsentWelcomeToConsentPage -> consentWelcomeToConsentPage(action.id)
            is ConsentWelcomeToConsentQuestion -> consentWelcomeToConsentQuestion(action.index)
            is ConsentPageToConsentPage -> consentPageToConsentPage(action.id)
            is ConsentPageToConsentQuestion -> consentPageToConsentQuestion(action.index)
            is ConsentQuestionToConsentQuestion -> consentQuestionToConsentQuestion(action.index)
            is ConsentQuestionToConsentSuccess -> consentQuestionToConsentSuccess()
            is ConsentQuestionToConsentFailure -> consentQuestionToConsentFailure()
            is ConsentFailureToConsentWelcome -> consentFailureToConsentWelcome()
            is ConsentFailureToConsentPage -> consentFailureToConsentPage(action.id)

            else -> {
                {}
            }
        }
}