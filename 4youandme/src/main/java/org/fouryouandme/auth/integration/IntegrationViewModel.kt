package org.fouryouandme.auth.integration

import androidx.navigation.NavController
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.openApp
import org.fouryouandme.core.arch.navigation.playStoreAction
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.integration.IntegrationUseCase
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync


class IntegrationViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        IntegrationState,
        IntegrationStateUpdate,
        IntegrationError,
        IntegrationLoading>
    (IntegrationState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(IntegrationLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    IntegrationUseCase.getIntegration(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, rootNavController, navigator)

            !initialization.fold(
                { setError(it, IntegrationError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            integration = pair.first.toOption(),
                            configuration = pair.second.toOption()
                        ),
                        IntegrationStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(IntegrationLoading.Initialization)

        }.unsafeRunAsync()

    fun getCookies(): Unit =
        runtime.fx.concurrent {

            val cookies =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryOrDisk)
                    .mapResult(runtime.fx) { mapOf("token" to it) }
                    .map { it.getOrElse { emptyMap() } }

            !setState(
                state().copy(cookies = cookies.some()),
                IntegrationStateUpdate.Cookies(cookies)
            )

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun nextPage(
        navController: NavController,
        page: Option<Page>,
        fromWelcome: Boolean = false
    ): Unit =
        runtime.fx.concurrent {

            !page.fold(
                {
                    navigator.navigateTo(
                        runtime,
                        navController,
                        if (fromWelcome) IntegrationWelcomeToIntegrationSuccess
                        else IntegrationPageToIntegrationSuccess
                    )
                },
                {
                    navigator.navigateTo(
                        runtime,
                        navController,
                        if (fromWelcome) IntegrationWelcomeToIntegrationPage(it.id)
                        else IntegrationPageToIntegrationPage(it.id)
                    )
                })

        }.unsafeRunAsync()


    fun handleSpecialLink(specialLinkAction: SpecialLinkAction): Unit =
        runtime.fx.concurrent {

            !when (specialLinkAction) {
                is SpecialLinkAction.OpenApp ->
                    navigator.performAction(
                        runtime,
                        openApp(specialLinkAction.app.packageName)
                    )
                is SpecialLinkAction.Download ->
                    navigator.performAction(
                        runtime,
                        playStoreAction(specialLinkAction.app.packageName)
                    )

            }

        }.unsafeRunAsync()

    fun login(
        navController: NavController,
        link: String,
        nextPage: Option<Page>,
        fromWelcome: Boolean
    ): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if (fromWelcome) IntegrationWelcomeToIntegrationLogin(link, nextPage.map { it.id })
            else IntegrationPageToIntegrationLogin(link, nextPage.map { it.id })
        ).unsafeRunAsync()

    fun handleLogin(
        navController: NavController,
        nextPageId: Option<String>
    ): Unit =
        runtime.fx.concurrent {

            !nextPageId.fold(
                {
                    navigator.navigateTo(
                        runtime,
                        navController,
                        IntegrationLoginToIntegrationSuccess
                    )
                },
                {
                    navigator.navigateTo(
                        runtime,
                        navController,
                        IntegrationLoginToIntegrationPage(it)
                    )
                }
            )

        }.unsafeRunAsync()

    fun openMain(rootNavController: RootNavController): Unit =
        navigator.navigateTo(runtime, rootNavController, IntegrationSuccessToMain).unsafeRunAsync()

}