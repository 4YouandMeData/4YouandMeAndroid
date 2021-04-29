package com.foryouandme.ui.auth.onboarding.step.consent.user

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController

abstract class ConsentUserSectionFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val viewModel: ConsentUserViewModel by viewModels(
        ownerProducer = { consentUserFragment() }
    )

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

    fun consentUserFragment(): ConsentUserFragment = find()

    fun authNavController(): AuthNavController = consentUserFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentUserFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        consentUserFragment().consentNavController()

    fun consentUserNavController(): ConsentUserNavController =
        ConsentUserNavController(findNavController())

    fun back(): Boolean =
        if (navigator.back(consentUserNavController()).not())
            if (navigator.back(consentNavController()).not())
                if (navigator.back(onboardingStepNavController()).not())
                    if (navigator.back(authNavController()).not())
                        navigator.back(rootNavController())
                    else true
                else true
            else true
        else true

}