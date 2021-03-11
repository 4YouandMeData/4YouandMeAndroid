package com.foryouandme.ui.auth.onboarding.step.consent

import com.foryouandme.ui.auth.onboarding.step.OnboardingStep

object OptInStep : OnboardingStep("opt_in", { ConsentFragment.build(true) })