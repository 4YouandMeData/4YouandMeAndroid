package com.foryouandme.ui.auth.onboarding.step.consent

import com.foryouandme.ui.auth.onboarding.step.OnboardingStep

object OptInStep : OnboardingStep("opt-ins", { ConsentFragment.build(true) })