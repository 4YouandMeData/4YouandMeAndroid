package com.foryouandme.auth.onboarding.step.consent

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator

abstract class ConsentSectionFragment<T : BaseViewModel<*, *, *, *>>(
    contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    val consentViewModel: ConsentViewModel by lazy {
        viewModelFactory(
            consentFragment(),
            getFactory {
                ConsentViewModel(navigator)
            }
        )
    }

    fun authNavController(): AuthNavController = consentFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentFragment().onboardingStepNavController()

    fun consentFragment(): ConsentFragment = find()

    fun consentNavController(): ConsentNavController = ConsentNavController(findNavController())

}