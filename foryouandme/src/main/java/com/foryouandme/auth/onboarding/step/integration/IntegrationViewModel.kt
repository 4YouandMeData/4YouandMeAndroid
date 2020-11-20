package com.foryouandme.auth.onboarding.step.integration

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.IntegrationModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.openApp
import com.foryouandme.core.arch.navigation.playStoreAction
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.integration.IntegrationUseCase.getIntegration
import com.foryouandme.core.entity.page.Page
import com.foryouandme.core.ext.web.asIntegrationCookies


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
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(integrationNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }

    suspend fun nextPage(
        integrationNavController: IntegrationNavController,
        page: Page?,
        fromWelcome: Boolean = false
    ): Unit {

        if (page == null)
            navigator.navigateTo(
                integrationNavController,
                if (fromWelcome) IntegrationWelcomeToIntegrationSuccess
                else IntegrationPageToIntegrationSuccess
            )
        else
            navigator.navigateTo(
                integrationNavController,
                if (fromWelcome) IntegrationWelcomeToIntegrationPage(page.id)
                else IntegrationPageToIntegrationPage(page.id)
            )


    }


    suspend fun handleSpecialLink(specialLinkAction: SpecialLinkAction): Unit {

        when (specialLinkAction) {
            is SpecialLinkAction.OpenApp ->
                navigator.performAction(openApp(specialLinkAction.app.packageName))
            is SpecialLinkAction.Download ->
                navigator.performAction(playStoreAction(specialLinkAction.app.packageName))
        }

    }

    suspend fun pageToLogin(
        integrationNavController: IntegrationNavController,
        link: String,
        nextPage: Page?,
    ): Unit =
        navigator.navigateTo(
            integrationNavController,
            IntegrationPageToIntegrationLogin(link, nextPage?.id)
        )

    suspend fun welcomeToLogin(
        integrationNavController: IntegrationNavController,
        link: String,
        nextPage: Page?
    ): Unit =
        navigator.navigateTo(
            integrationNavController,
            IntegrationWelcomeToIntegrationLogin(link, nextPage?.id)
        )

    suspend fun handleLogin(
        integrationNavController: IntegrationNavController,
        nextPageId: String?
    ): Unit {

        if (nextPageId == null)
            navigator.navigateTo(
                integrationNavController,
                IntegrationLoginToIntegrationSuccess
            )
        else
            navigator.navigateTo(
                integrationNavController,
                IntegrationLoginToIntegrationPage(nextPageId)
            )

    }



    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.OAuth, EAnalyticsProvider.ALL)

}