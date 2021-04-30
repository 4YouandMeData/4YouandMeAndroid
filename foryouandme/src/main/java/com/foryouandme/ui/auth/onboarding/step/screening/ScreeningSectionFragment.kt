package com.foryouandme.ui.auth.onboarding.step.screening

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import kotlinx.coroutines.flow.onEach

abstract class ScreeningSectionFragment(
    contentLayoutId: Int
) : BaseFragment(contentLayoutId) {

    val viewModel: ScreeningViewModel by viewModels(ownerProducer = { screeningFragment() })

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

    /* --- navigation --- */

    fun screeningFragment(): ScreeningFragment = find()

    fun authNavController(): AuthNavController = screeningFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        screeningFragment().onboardingStepNavController()

    fun screeningNavController(): ScreeningNavController =
        ScreeningNavController(findNavController())

    fun back(): Boolean =
        if (navigator.back(screeningNavController()).not())
            if (navigator.back(onboardingStepNavController()).not())
                if (navigator.back(authNavController()).not())
                    navigator.back(rootNavController())
                else true
            else true
        else true

    fun questions(fromWelcome: Boolean) {
        navigator.navigateTo(
            screeningNavController(),
            if (fromWelcome) ScreeningWelcomeToScreeningQuestions
            else ScreeningPageToScreeningQuestions
        )
    }

    fun page(id: String, fromWelcome: Boolean) {
        navigator.navigateTo(
            screeningNavController(),
            if (fromWelcome) ScreeningWelcomeToScreeningPage(id)
            else ScreeningPageToScreeningPage(id)
        )
    }

}