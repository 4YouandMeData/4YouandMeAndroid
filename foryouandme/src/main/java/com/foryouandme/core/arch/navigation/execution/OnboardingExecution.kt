package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.ui.auth.onboarding.step.OnboardingStepContainerFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.auth.AuthFragmentDirections

fun onboardingStepToOnboardingStep(index: Int): NavigationExecution = {

    it.navigate(
        OnboardingStepContainerFragmentDirections.actionOnboardingStepToOnboardingStep(index)
    )

}

fun onboardingToMain(): NavigationExecution = {

    it.navigate(
        AuthFragmentDirections.actionAuthToMain()
    )

}