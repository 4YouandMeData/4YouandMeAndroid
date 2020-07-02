package org.fouryouandme.auth.optin

import androidx.navigation.NavController
import arrow.core.firstOrNone
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.optins.OptInsUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync


class OptInViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        OptInState,
        OptInStateUpdate,
        OptInError,
        OptInLoading>
    (OptInState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(OptInLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    OptInsUseCase.getOptIns(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, rootNavController, navigator)

            !initialization.fold(
                { setError(it, OptInError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            optIns = pair.first.toOption(),
                            configuration = pair.second.toOption()
                        ),
                        OptInStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(OptInLoading.Initialization)

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun sectionBack(navController: RootNavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun permission(navController: NavController): Unit =
        runtime.fx.concurrent {

            !state().optIns.flatMap { it.permissions.firstOrNone() }
                .fold(
                    { just(Unit) },
                    {
                        navigator.navigateTo(
                            runtime,
                            navController,
                            OptInWelcomeToOptInPermission(it.id)
                        )
                    }
                )

        }.unsafeRunAsync()

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()

}