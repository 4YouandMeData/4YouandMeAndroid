package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.auth.onboarding.step.OnboardingStepContainerFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

fun onboardingStepToOnboardingStep(index: Int): NavigationExecution = {

    it.navigate(
        OnboardingStepContainerFragmentDirections.actionOnboardingStepToOnboardingStep(index)
    )

}