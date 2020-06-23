package org.fouryouandme.auth.consent.review

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewHeaderItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase
import org.fouryouandme.core.entity.page.Page
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

    fun initialize(navController: RootNavController): Unit =
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

                    val items = mutableListOf<DroidItem>()

                    items.addAll(
                        pair.first.welcomePage
                            .toItems()
                            .map { it.toConsentReviewPageItem(pair.second) }
                    )

                    items.add(0, pair.first.toConsentReviewHeaderItem(pair.second))

                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            consentReview = pair.first.toOption(),
                            items = items
                        ),
                        ConsentReviewStateUpdate.Initialization(pair.second, pair.first, items)
                    )
                }
            )

            !hideLoading(ConsentReviewLoading.Initialization)

        }.unsafeRunAsync()


    private fun Page.toItems(): MutableList<Page> {

        var page = this.toOption()

        val items = mutableListOf(this)

        while (page.flatMap { it.link1 }.isDefined()) {

            val nextPage = page.flatMap { it.link1 }

            nextPage.map { items.add(it) }

            page = nextPage

        }

        return items

    }

    /* --- navigation --- */

    fun disagree(navController: NavController): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentReviewInfoToConsentReviewDisagree
        ).unsafeRunAsync()

    fun exit(navController: NavController): Unit =
        navigator.navigateTo(runtime, navController, ConsentReviewDisagreeToAuth).unsafeRunAsync()

    fun consentUser(rootNavController: RootNavController): Unit =
        navigator.navigateTo(
            runtime,
            rootNavController,
            ConsentReviewToConsentUser
        ).unsafeRunAsync()

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()
}