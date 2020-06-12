package org.fouryouandme.auth.consent

import androidx.navigation.NavController
import arrow.core.None
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.ForIO
import arrow.syntax.function.pipe
import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.auth.screening.questions.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWelcome
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.ConsentUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync

class ConsentViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ConsentState,
        ConsentStateUpdate,
        ConsentError,
        ConsentLoading>
    (ConsentState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ConsentUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ConsentError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            consent = pair.first.toOption()
                        ),
                        ConsentStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(ConsentLoading.Initialization)

        }.unsafeRunAsync()


    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun page(navController: NavController, id: String, fromWelcome: Boolean): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if(fromWelcome) ConsentWelcomeToConsentPage(id) else ConsentPageToConsentPage(id)
        ).unsafeRunAsync()

    fun question(navController: NavController, index: Int, fromWelcome: Boolean): Unit =
        when {
            fromWelcome -> ConsentWelcomeToConsentQuestion(index)
            index == 0 -> ConsentPageToConsentQuestion(index)
            else -> ConsentQuestionToConsentQuestion(index)
        }.pipe { navigator.navigateTo(runtime, navController, it) }.unsafeRunAsync()
}