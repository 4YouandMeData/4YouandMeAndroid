package org.fouryouandme.auth.consent.review

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.auth.consent.review.info.toConsentReviewHeaderItem
import org.fouryouandme.auth.consent.review.info.toConsentReviewPageItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import org.fouryouandme.core.entity.configuration.Configuration

class ConsentReviewViewModel(
    navigator: Navigator,
    private val consentReviewModule: ConsentReviewModule
) : BaseViewModel<
        ConsentReviewState,
        ConsentReviewStateUpdate,
        ConsentReviewError,
        ConsentReviewLoading>
    (navigator = navigator) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Either<FourYouAndMeError, ConsentReviewState> {

        showLoading(ConsentReviewLoading.Initialization)

        val state =
            consentReviewModule.getConsent()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, ConsentReviewError.Initialization)
                        it.left()
                    },
                    { consentReview ->

                        val items = mutableListOf<DroidItem<Any>>()

                        items.addAll(
                            consentReview.welcomePage
                                .asList()
                                .map { it.toConsentReviewPageItem(configuration) }
                        )

                        items.add(0, consentReview.toConsentReviewHeaderItem(configuration))

                        val state =
                            ConsentReviewState(consentReview, items)

                        setState(state)
                        { ConsentReviewStateUpdate.Initialization(it.consentReview, it.items) }

                        state.right()

                    }
                )

        hideLoading(ConsentReviewLoading.Initialization)

        return state

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