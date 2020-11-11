package com.foryouandme.auth.consent.review

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.consent.review.info.toConsentReviewHeaderItem
import com.foryouandme.auth.consent.review.info.toConsentReviewPageItem
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.ConsentReviewModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.consent.review.ConsentReviewUseCase.getConsent
import com.foryouandme.core.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem

class ConsentReviewViewModel(
    navigator: Navigator,
    private val consentReviewModule: ConsentReviewModule,
    private val analyticsModule: AnalyticsModule
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
    ): Either<ForYouAndMeError, ConsentReviewState> {

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

    suspend fun disagree(consentReviewNavController: ConsentReviewNavController): Unit {

        logDisagree()

        navigator.navigateTo(
            consentReviewNavController,
            ConsentReviewInfoToConsentReviewDisagree
        )

    }

    suspend fun exit(rootNavController: RootNavController): Unit =
        navigator.navigateTo(
            rootNavController,
            ConsentReviewDisagreeToAuth
        )

    suspend fun optIns(authNavController: AuthNavController): Unit {

        logAgree()

        navigator.navigateTo(
            authNavController,
            ConsentReviewToOptIns
        )
    }

    suspend fun back(
        consentReviewNavController: ConsentReviewNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(consentReviewNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }

    /* --- analytics --- */

    private suspend fun logAgree(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ConsentAgreed, EAnalyticsProvider.ALL)


    private suspend fun logDisagree(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ConsentDisagreed, EAnalyticsProvider.ALL)

}