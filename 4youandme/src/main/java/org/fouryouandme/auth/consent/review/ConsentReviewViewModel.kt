package org.fouryouandme.auth.consent.review

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync

class ConsentReviewViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ConsentReviewState,
        ConsentReviewStateUpdate,
        ConsentReviewError,
        ConsentReviewLoading>
    (ConsentReviewState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentReviewLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ConsentReviewUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ConsentReviewError.Initialization) },
                { pair ->

                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            consentReview = pair.first.toOption()
                        ),
                        ConsentReviewStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(ConsentReviewLoading.Initialization)

        }.unsafeRunAsync()

}