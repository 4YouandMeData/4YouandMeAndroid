package com.foryouandme.auth.onboarding.step

open class OnboardingStep(

    val identifier: String,
    val view: () -> OnboardingStepFragment<*>
)