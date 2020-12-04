package com.foryouandme.ui.auth.onboarding.step.consent.informed

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync

abstract class ConsentInfoSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentInfoViewModel>(contentLayoutId) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            consentInfoFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    injector.consentInfoModule(),
                    injector.answerModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    startCoroutineAsync {

                        val back =
                            viewModel.back(
                                consentInfoNavController(),
                                consentNavController(),
                                onboardingStepNavController(),
                                authNavController(),
                                rootNavController()
                            )

                        if (back.not()) requireActivity().finish()

                    }
                }

            }
        )
    }

    fun consentInfoFragment(): ConsentInfoFragment = find()

    fun authNavController(): AuthNavController = consentInfoFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentInfoFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        consentInfoFragment().consentNavController()

    fun consentInfoNavController(): ConsentInfoNavController =
        ConsentInfoNavController(findNavController())

    fun consentInfoAndConfiguration(block: suspend (Configuration, ConsentInfoState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), config).orNull()
                    ?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}