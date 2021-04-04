package com.foryouandme.ui.auth.onboarding.step.consent.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.consent.GetConsentReviewUseCase
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.toConsentReviewHeaderItem
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.toConsentReviewPageItem
import com.giacomoparisi.recyclerdroid.core.DroidItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsentReviewViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ConsentReviewStateUpdate>,
    private val loadingFlow: LoadingFlow<ConsentReviewLoading>,
    private val errorFlow: ErrorFlow<ConsentReviewError>,
    private val getConsentReviewUseCase: GetConsentReviewUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = ConsentReviewState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- consent review --- */

    private suspend fun getConsentReview(configuration: Configuration) {

        loadingFlow.show(ConsentReviewLoading.ConsentReview)

        val consentReview = getConsentReviewUseCase()!!
        val items = mutableListOf<DroidItem<Any>>()

        items.addAll(
            consentReview.welcomePage
                .asList(consentReview.pages)
                .map { it.toConsentReviewPageItem(configuration) }
        )

        items.add(0, consentReview.toConsentReviewHeaderItem(configuration))

        state = state.copy(consentReview = consentReview, items = items)
        stateUpdateFlow.update(ConsentReviewStateUpdate.ConsentReview)

        loadingFlow.hide(ConsentReviewLoading.ConsentReview)

    }

    /* --- analytics --- */

    private suspend fun logAgree(): Unit =
        sendAnalyticsEventUseCase(AnalyticsEvent.ConsentAgreed, EAnalyticsProvider.ALL)


    private suspend fun logDisagree(): Unit =
        sendAnalyticsEventUseCase(AnalyticsEvent.ConsentDisagreed, EAnalyticsProvider.ALL)

    /* --- state event --- */

    fun execute(stateEvent: ConsentReviewStateEvent) {
        when (stateEvent) {
            is ConsentReviewStateEvent.GetConsentReview ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentReviewError.ConsentReview,
                    loadingFlow,
                    ConsentReviewLoading.ConsentReview
                ) { getConsentReview(stateEvent.configuration) }
            ConsentReviewStateEvent.Agree ->
                viewModelScope.launchSafe { logAgree() }
            ConsentReviewStateEvent.Disagree ->
                viewModelScope.launchSafe { logDisagree() }
        }
    }

}