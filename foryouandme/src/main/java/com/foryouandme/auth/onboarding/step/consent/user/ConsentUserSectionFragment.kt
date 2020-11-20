package com.foryouandme.auth.onboarding.step.consent.user

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

abstract class ConsentUserSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentUserViewModel>(contentLayoutId) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            consentUserFragment(),
            getFactory {
                ConsentUserViewModel(
                    navigator,
                    injector.consentUserModule(),
                    injector.analyticsModule()
                )
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

    fun consentUserAndConfiguration(block: suspend (Configuration, ConsentUserState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController()).orNull()?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}