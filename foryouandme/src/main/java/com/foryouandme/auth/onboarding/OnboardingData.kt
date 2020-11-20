package com.foryouandme.auth.onboarding

import com.foryouandme.auth.onboarding.step.OnboardingStep
import com.foryouandme.core.arch.navigation.NavigationAction

data class OnboardingState(val onboardingSteps: List<OnboardingStep>)

sealed class OnboardingStateUpdate {

    data class Initialization(val onboardingSteps: List<OnboardingStep>) : OnboardingStateUpdate()

}

/* --- navigation --- */

data class OnboardingStepToOnboardingStep(val index: Int) : NavigationAction

object OnboardingToMain: NavigationAction