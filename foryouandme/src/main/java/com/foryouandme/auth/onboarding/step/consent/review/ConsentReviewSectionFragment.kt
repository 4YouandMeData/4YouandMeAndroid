package com.foryouandme.auth.onboarding.step.consent.review

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

abstract class ConsentReviewSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentReviewViewModel>(contentLayoutId) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            consentReviewFragment(),
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    injector.consentReviewModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    fun consentReviewFragment(): ConsentReviewFragment = find()

    fun authNavController(): AuthNavController = consentReviewFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentReviewFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        consentReviewFragment().consentNavController()

    fun consentReviewNavController(): ConsentReviewNavController =
        ConsentReviewNavController(findNavController())

    fun consentReviewAndConfiguration(block: suspend (Configuration, ConsentReviewState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), config).orNull()
                    ?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}