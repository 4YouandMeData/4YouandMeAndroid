package com.foryouandme.ui.auth.onboarding.step.consent.informed

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.ui.web.EWebPageType
import kotlinx.coroutines.flow.onEach

abstract class ConsentInfoSectionFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val viewModel: ConsentInfoViewModel by viewModels(ownerProducer = { consentInfoFragment() })

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

    fun consentInfoFragment(): ConsentInfoFragment = find()

    fun authNavController(): AuthNavController = consentInfoFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentInfoFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        consentInfoFragment().consentNavController()

    fun consentInfoNavController(): ConsentInfoNavController =
        ConsentInfoNavController(findNavController())

    fun back(): Boolean =
        if (navigator.back(consentInfoNavController()).not())
            if (navigator.back(consentNavController()).not())
                if (navigator.back(onboardingStepNavController()).not())
                    if (navigator.back(authNavController()).not())
                        navigator.back(rootNavController())
                    else true
                else true
            else true
        else true

    fun question(fromWelcome: Boolean) {
        when {
            fromWelcome -> ConsentInfoWelcomeToConsentInfoQuestion(
                0
            )
            else -> ConsentInfoPageToConsentInfoQuestion(
                0
            )
        }.let { navigator.navigateTo(consentInfoNavController(), it) }
    }

    fun page(id: String, fromWelcome: Boolean) {
        navigator.navigateTo(
            consentInfoNavController(),
            if (fromWelcome) ConsentInfoWelcomeToConsentInfoPage(id)
            else ConsentInfoPageToConsentInfoPage(id)
        )
    }

    fun web(url: String, type: EWebPageType) {
        navigator.navigateTo(rootNavController(), AnywhereToWeb(url, type))
    }

}