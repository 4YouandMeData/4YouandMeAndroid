package com.foryouandme.ui.auth.onboarding.step.integration

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync

abstract class IntegrationSectionFragment(
    contentLayoutId: Int
) : BaseFragment<IntegrationViewModel>(contentLayoutId) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            integrationFragment(),
            getFactory {
                IntegrationViewModel(
                    navigator,
                    injector.authModule(),
                    injector.integrationModule(),
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
                                integrationNavController(),
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

    fun authNavController(): AuthNavController = integrationFragment().authNavController()

    fun integrationFragment(): IntegrationFragment = find()

    fun integrationNavController(): IntegrationNavController =
        IntegrationNavController(findNavController())

    fun onboardingStepNavController(): OnboardingStepNavController =
        integrationFragment().onboardingStepNavController()

    fun integrationAndConfiguration(block: suspend (Configuration, IntegrationState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController()).orNull()?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}