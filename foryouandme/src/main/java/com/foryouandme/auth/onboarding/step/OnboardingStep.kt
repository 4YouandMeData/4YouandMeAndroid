package com.foryouandme.auth.onboarding.step

class OnboardingStep(

    val identifier: String,
    val view: () -> OnboardingStepFragment<*>
)