package org.fouryouandme.main.app.navigation

import org.fouryouandme.auth.phone.EnterPhoneToPhoneValidationCode
import org.fouryouandme.auth.phone.EnterPhoneToWeb
import org.fouryouandme.auth.phone.code.PhoneValidationCodeToScreening
import org.fouryouandme.auth.screening.ScreeningQuestionsToScreeningFailure
import org.fouryouandme.auth.screening.ScreeningQuestionsToScreeningSuccess
import org.fouryouandme.auth.screening.ScreeningWelcomeToScreeningQuestions
import org.fouryouandme.auth.signup.info.SignUpInfoToEnterPhone
import org.fouryouandme.auth.signup.info.SignUpInfoToSignUpLater
import org.fouryouandme.auth.splash.SplashToWelcome
import org.fouryouandme.auth.welcome.WelcomeToSignUpInfo
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.core.arch.navigation.NavigationProvider
import org.fouryouandme.main.app.navigation.execution.*

class ForYouAndMeNavigationProvider : NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution =
        when (action) {

            is SplashToWelcome -> splashToWelcome()
            is WelcomeToSignUpInfo -> welcomeToSignUpInfo()
            is SignUpInfoToSignUpLater -> signUpInfoToSignUpLater()
            is SignUpInfoToEnterPhone -> signUpInfoToEnterPhone()
            is EnterPhoneToPhoneValidationCode -> enterPhoneToPhoneValidationCode(
                action.phone,
                action.countryCode
            )
            is EnterPhoneToWeb -> enterPhoneToWeb(action.url)
            is PhoneValidationCodeToScreening -> phoneValidationCodeToScreening()

            is ScreeningWelcomeToScreeningQuestions -> screeningWelcomeToScreeningQuestions()
            is ScreeningQuestionsToScreeningSuccess -> screeningQuestionsToScreeningSuccess()
            is ScreeningQuestionsToScreeningFailure -> screeningQuestionsToScreeningFailure()

            else -> {
                {}
            }
        }
}