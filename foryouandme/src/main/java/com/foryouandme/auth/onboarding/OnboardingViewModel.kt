package com.foryouandme.auth.onboarding

import com.foryouandme.auth.onboarding.step.OnboardingStep
import com.foryouandme.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.ext.foldSuspend

class OnboardingViewModel(
    navigator: Navigator
) : BaseViewModel<OnboardingState, OnboardingStateUpdate, Empty, Empty>(navigator) {

    /* --- steps --- */

    fun getStepByIndex(index: Int): OnboardingStep? = state().onboardingSteps.getOrNull(index)

    suspend fun nextStep(
        rootNavController: RootNavController,
        onboardingStepNavController: OnboardingStepNavController,
        currentStepIndex: Int
    ): Unit =
        getStepByIndex(currentStepIndex + 1)
            .foldSuspend(
                { end(rootNavController) },
                {
                    navigator.navigateTo(
                        onboardingStepNavController,
                        OnboardingStepToOnboardingStep(currentStepIndex + 1)
                    )
                }
            )

    suspend fun end(rootNavController: RootNavController): Unit =
        navigator.navigateTo(rootNavController, OnboardingToMain)

}