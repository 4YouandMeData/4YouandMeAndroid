package com.foryouandme.ui.auth.onboarding.step.consent

import com.foryouandme.ui.auth.onboarding.step.OnboardingStep

object ConsentStep : OnboardingStep("consent_group", { ConsentFragment.build(false) })