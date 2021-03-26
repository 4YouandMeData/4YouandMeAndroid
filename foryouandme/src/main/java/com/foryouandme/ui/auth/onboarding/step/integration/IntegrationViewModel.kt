package com.foryouandme.ui.auth.onboarding.step.integration

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.IntegrationModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.action.openApp
import com.foryouandme.core.arch.navigation.action.playStoreAction
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.integration.IntegrationUseCase.getIntegration
import com.foryouandme.core.ext.web.asIntegrationCookies
import com.foryouandme.entity.page.PageRef


class IntegrationViewModel(
    navigator: Navigator,
    private val authModule: AuthModule,
    private val integrationModule: IntegrationModule,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        IntegrationState,
        IntegrationStateUpdate,
        IntegrationError,
        IntegrationLoading>
    (navigator = navigator) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<ForYouAndMeError, IntegrationState> {

        showLoading(IntegrationLoading.Initialization)

        val state =
            integrationModule.getIntegration()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, IntegrationError.Initialization)
                        it.left()
                    },
                    { integration ->

                        val cookies =
                            authModule.getToken(CachePolicy.MemoryOrDisk)
                                .map { it.asIntegrationCookies() }
                                .getOrElse { emptyMap() }

                        val state =
                            IntegrationState(integration, cookies)

                        setState(state)
                        { IntegrationStateUpdate.Initialization(it.integration) }

                        state.right()

                    }
                )

        hideLoading(IntegrationLoading.Initialization)

        return state

    }

    suspend fun getCookies(): Unit {

        val cookies =
            authModule.getToken(CachePolicy.MemoryOrDisk).map { mapOf("token" to it) }
                .getOrElse { emptyMap() }

        setState(state().copy(cookies = cookies)) { IntegrationStateUpdate.Cookies(cookies) }

    }

    /* --- navigation --- */

    suspend fun back(
        integrationNavController: IntegrationNavController,
        onboardingStepNavController: OnboardingStepNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Boolean =
        if (navigator.backSuspend(integrationNavController).not())
            if (navigator.backSuspend(onboardingStepNavController).not())
                if (navigator.backSuspend(authNavController).not())
                    navigator.backSuspend(rootNavController)
                else true
            else true
        else true

    suspend fun nextPage(
        integrationNavController: IntegrationNavController,
        page: PageRef?,
        fromWelcome: Boolean = false
    ): Unit {

        if (page == null)
            navigator.navigateToSuspend(
                integrationNavController,
                if (fromWelcome) IntegrationWelcomeToIntegrationSuccess
                else IntegrationPageToIntegrationSuccess
            )
        else
            navigator.navigateToSuspend(
                integrationNavController,
                if (fromWelcome) IntegrationWelcomeToIntegrationPage(page.id)
                else IntegrationPageToIntegrationPage(page.id)
            )


    }


    suspend fun handleSpecialLink(specialLinkAction: SpecialLinkAction): Unit {

        when (specialLinkAction) {
            is SpecialLinkAction.OpenApp ->
                navigator.performActionSuspend(openApp(specialLinkAction.app.packageName))
            is SpecialLinkAction.Download ->
                navigator.performActionSuspend(playStoreAction(specialLinkAction.app.packageName))
        }

    }

    suspend fun pageToLogin(
        integrationNavController: IntegrationNavController,
        link: String,
        nextPage: PageRef?,
    ): Unit =
        navigator.navigateToSuspend(
            integrationNavController,
            IntegrationPageToIntegrationLogin(link, nextPage?.id)
        )

    suspend fun welcomeToLogin(
        integrationNavController: IntegrationNavController,
        link: String,
        nextPage: PageRef?
    ): Unit =
        navigator.navigateToSuspend(
            integrationNavController,
            IntegrationWelcomeToIntegrationLogin(link, nextPage?.id)
        )

    suspend fun handleLogin(
        integrationNavController: IntegrationNavController,
        nextPageId: String?
    ): Unit {

        if (nextPageId == null)
            navigator.navigateToSuspend(
                integrationNavController,
                IntegrationLoginToIntegrationSuccess
            )
        else
            navigator.navigateToSuspend(
                integrationNavController,
                IntegrationLoginToIntegrationPage(nextPageId)
            )

    }


    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.OAuth, EAnalyticsProvider.ALL)

}