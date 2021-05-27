package com.foryouandme.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.auth.consent.GetConsentReviewUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.ui.compose.items.consent.ConsentReviewPageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReviewConsentViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getConsentReviewUseCase: GetConsentReviewUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(ReviewConsentState())
    val stateFlow = state as StateFlow<ReviewConsentState>

    init {
        execute(ReviewConsentAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
                execute(ReviewConsentAction.GetReviewConsent)
            },
            {
                state.emit(state.value.copy(configuration = it.toError()))
            }
        )

    /* --- review consent --- */

    private fun getReviewConsent(): Action =
        action(
            {
                val configuration = state.value.configuration.dataOrNull()

                if (configuration != null) {
                    state.emit(
                        state.value.copy(
                            consentReview = state.value.consentReview.toLoading()
                        )
                    )
                    val consentReview = getConsentReviewUseCase()!!
                    val items = consentReview
                        .welcomePage
                        .asList(consentReview.pages)
                        .map { ConsentReviewPageItem.fromPage(it, configuration) }
                    state.emit(
                        state.value.copy(
                            consentReview = consentReview.toData(),
                            items = items
                        )
                    )
                }

            },
            {
                state.emit(state.value.copy(consentReview = it.toError()))
            }
        )

    /* --- action --- */

    fun execute(action: ReviewConsentAction) {
        when (action) {
            ReviewConsentAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            ReviewConsentAction.GetReviewConsent ->
                viewModelScope.launchAction(getReviewConsent())
        }
    }

}