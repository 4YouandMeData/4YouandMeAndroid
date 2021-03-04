package com.foryouandme.ui.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.consent.user.CompleteConsentUseCase
import com.foryouandme.ui.auth.onboarding.step.OnboardingStep
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentStep
import com.foryouandme.ui.auth.onboarding.step.consent.OptInStep
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationStep
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningStep
import com.foryouandme.ui.auth.onboarding.step.video.VideoStep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<OnboardingLoading>,
    private val errorFlow: ErrorFlow<OnboardingError>,
    private val stateUpdateFlow: StateUpdateFlow<OnboardingStateUpdate>,
    private val navigationFlow: NavigationFlow,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val completeUserConsentUseCase: CompleteConsentUseCase
) : ViewModel() {

    /* --- state --- */

    var state: OnboardingState = OnboardingState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- initialization --- */

    fun isInitialized(): Boolean =
        state.configuration != null && state.onboardingSteps.isNotEmpty()

    private suspend fun initialize() {

        loadingFlow.show(OnboardingLoading.Initialization)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        val steps =
            configuration.text.onboarding.sections.mapNotNull {

                when (it) {
                    "intro_video" -> VideoStep
                    "screening" -> ScreeningStep
                    "consent_group" -> ConsentStep
                    "opt-ins" -> OptInStep
                    "integration" -> IntegrationStep
                    else -> null
                }

            }

        state = state.copy(configuration = configuration, onboardingSteps = steps)
        stateUpdateFlow.update(OnboardingStateUpdate.Initialized(configuration, steps))

        loadingFlow.hide(OnboardingLoading.Initialization)

    }

    /* --- steps --- */

    fun getStepByIndex(index: Int): OnboardingStep? = state.onboardingSteps.getOrNull(index)

    private suspend fun nextStep(currentStepIndex: Int) {

        val nextStep = getStepByIndex(currentStepIndex + 1)

        if (nextStep == null) end()
        else
            navigationFlow.navigateTo(OnboardingStepToOnboardingStep(currentStepIndex + 1))

    }

    private suspend fun end() {
        loadingFlow.show(OnboardingLoading.NextStep)
        completeUserConsentUseCase()
        loadingFlow.hide(OnboardingLoading.NextStep)
        navigationFlow.navigateTo(OnboardingToMain)
    }

    /* --- state event --- */

    fun execute(stateEvent: OnboardingStateEvent) {

        when (stateEvent) {
            OnboardingStateEvent.Initialize ->
                errorFlow.launchCatch(
                    viewModelScope,
                    OnboardingError.Initialization,
                    loadingFlow,
                    OnboardingLoading.Initialization
                ) { initialize() }
            is OnboardingStateEvent.NextStep ->
                errorFlow.launchCatch(
                    viewModelScope,
                    OnboardingError.NextStep,
                    loadingFlow,
                    OnboardingLoading.NextStep
                ) { nextStep(stateEvent.currentStepIndex) }
        }

    }

}