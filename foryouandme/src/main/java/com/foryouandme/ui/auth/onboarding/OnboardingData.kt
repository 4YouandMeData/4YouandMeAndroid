package com.foryouandme.ui.auth.onboarding

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.onboarding.step.OnboardingStep

data class OnboardingState(
    val configuration: Configuration? = null,
    val onboardingSteps: List<OnboardingStep> = emptyList()
)

sealed class OnboardingStateUpdate {

    data class Initialized(
        val configuration: Configuration,
        val onboardingSteps: List<OnboardingStep>
    ) : OnboardingStateUpdate()

}

sealed class OnboardingLoading {

    object Initialization : OnboardingLoading()
    object NextStep: OnboardingLoading()

}

sealed class OnboardingError {

    object Initialization : OnboardingError()
    object NextStep: OnboardingError()

}

sealed class OnboardingStateEvent {

    object Initialize : OnboardingStateEvent()
    data class NextStep(val currentStepIndex: Int): OnboardingStateEvent()

}

/* --- navigation --- */

data class OnboardingStepToOnboardingStep(val index: Int) : NavigationAction

object OnboardingToMain : NavigationAction