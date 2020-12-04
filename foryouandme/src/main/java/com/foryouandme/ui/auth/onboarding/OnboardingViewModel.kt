package com.foryouandme.ui.auth.onboarding

import com.foryouandme.ui.auth.onboarding.step.OnboardingStep
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentStep
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationStep
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningStep
import com.foryouandme.ui.auth.onboarding.step.video.VideoStep
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.foldSuspend

class OnboardingViewModel(
    navigator: Navigator
) : BaseViewModel<OnboardingState, OnboardingStateUpdate, Empty, Empty>(navigator) {

    /* --- initialization --- */

    suspend fun initialize(configuration: Configuration): OnboardingState {

        val steps =
            configuration.text.onboarding.sections.mapNotNull {

                when (it) {
                    "intro_video" -> VideoStep
                    "screening" -> ScreeningStep
                    "consent_group" -> ConsentStep
                    "integration" -> IntegrationStep
                    else -> null
                }

            }

        val state = OnboardingState(steps)

        setState(state) { OnboardingStateUpdate.Initialization(it.onboardingSteps) }

        return state

    }

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