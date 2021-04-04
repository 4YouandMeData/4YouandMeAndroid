package com.foryouandme.ui.auth.onboarding.step.consent

import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController

abstract class ConsentSectionFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    fun authNavController(): AuthNavController = consentFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentFragment().onboardingStepNavController()

    fun consentFragment(): ConsentFragment = find()

    fun consentNavController(): ConsentNavController = ConsentNavController(findNavController())

}