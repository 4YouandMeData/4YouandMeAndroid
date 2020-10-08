package org.fouryouandme.auth.consent.review

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.auth.consent.review.info.toConsentReviewHeaderItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page

class ConsentReviewViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val consentReviewModule: ConsentReviewModule
) : BaseViewModel<
        ForIO,
        ConsentReviewState,
        ConsentReviewStateUpdate,
        ConsentReviewError,
        ConsentReviewLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Either<FourYouAndMeError, ConsentReviewState> {

        showLoadingFx(ConsentReviewLoading.Initialization)

        val state =
            consentReviewModule.getConsent()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setErrorFx(it, ConsentReviewError.Initialization)
                        it.left()
                    },
                    { consentReview ->

                        val items = mutableListOf<DroidItem<Any>>()

                        items.addAll(
                            consentReview.welcomePage
                                .toItems()
                                .map { it.toConsentReviewPageItem(configuration) }
                        )

                        items.add(0, consentReview.toConsentReviewHeaderItem(configuration))

                        val state =
                            ConsentReviewState(consentReview, items)

                        setStateFx(state)
                        { ConsentReviewStateUpdate.Initialization(it.consentReview, it.items) }

                        state.right()

                    }
                )

        hideLoadingFx(ConsentReviewLoading.Initialization)

        return state

    }

    private fun Page.toItems(): MutableList<Page> {

        var page = this

        val items = mutableListOf(this)

        while (page.link1 != null) {

            items.add(page.link1!!)

            page = page.link1!!

        }

        return items

    }

    /* --- navigation --- */

    suspend fun disagree(consentReviewNavController: ConsentReviewNavController): Unit =
        navigator.navigateTo(
            consentReviewNavController,
            ConsentReviewInfoToConsentReviewDisagree
        )

    suspend fun exit(rootNavController: RootNavController): Unit =
        navigator.navigateTo(
            rootNavController,
            ConsentReviewDisagreeToAuth
        )

    suspend fun optIns(authNavController: AuthNavController): Unit =
        navigator.navigateTo(
            authNavController,
            ConsentReviewToOptIns
        )

    suspend fun back(
        consentReviewNavController: ConsentReviewNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(consentReviewNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }
}