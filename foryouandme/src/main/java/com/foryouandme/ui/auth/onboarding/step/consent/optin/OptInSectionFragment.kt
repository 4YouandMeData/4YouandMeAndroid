package com.foryouandme.ui.auth.onboarding.step.consent.optin

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController

abstract class OptInSectionFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val viewModel: OptInViewModel by viewModels(ownerProducer = { optInFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    if (back().not()) requireActivity().finish()
                }

            }
        )
    }

    fun optInFragment(): OptInFragment = find()

    fun authNavController(): AuthNavController = optInFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        optInFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        optInFragment().consentNavController()

    fun optInNavController(): OptInNavController =
        OptInNavController(findNavController())

    fun back(): Boolean =
        if (navigator.back(optInNavController()).not())
            if (navigator.back(consentNavController()).not())
                if (navigator.back(onboardingStepNavController()).not())
                    if (navigator.back(authNavController()).not())
                        navigator.back(rootNavController())
                    else true
                else true
            else true
        else true

}