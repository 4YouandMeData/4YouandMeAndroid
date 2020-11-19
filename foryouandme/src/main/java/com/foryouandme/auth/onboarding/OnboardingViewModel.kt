package com.foryouandme.auth.onboarding

import com.foryouandme.auth.onboarding.step.OnboardingStep
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator

class OnboardingViewModel(
    navigator: Navigator
) : BaseViewModel<OnboardingState, OnboardingStateUpdate, Empty, Empty>(navigator) {

    /* --- steps --- */

    fun getStepByIndex(index: Int): OnboardingStep? = state().onboardingSteps.getOrNull(index)

}