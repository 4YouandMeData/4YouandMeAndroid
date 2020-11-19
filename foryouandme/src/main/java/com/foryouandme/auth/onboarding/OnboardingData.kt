package com.foryouandme.auth.onboarding

import com.foryouandme.auth.onboarding.step.OnboardingStep

data class OnboardingState(val onboardingSteps: List<OnboardingStep>)

sealed class OnboardingStateUpdate {

    data class Initialization(val onboardingSteps: List<OnboardingStep>): OnboardingStateUpdate()

}