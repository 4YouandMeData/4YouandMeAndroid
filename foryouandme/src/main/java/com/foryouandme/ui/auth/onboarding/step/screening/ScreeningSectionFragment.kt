package com.foryouandme.ui.auth.onboarding.step.screening

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync

abstract class ScreeningSectionFragment(
    contentLayoutId: Int
) : BaseFragmentOld<ScreeningViewModel>(contentLayoutId) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            screeningFragment(),
            getFactory {
                ScreeningViewModel(
                    navigator,
                    injector.screeningModule(),
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
                                screeningNavController(),
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

    fun screeningFragment(): ScreeningFragment = find()

    fun authNavController(): AuthNavController = screeningFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        screeningFragment().onboardingStepNavController()

    fun screeningNavController(): ScreeningNavController =
        ScreeningNavController(findNavController())

    fun screeningAndConfiguration(block: suspend (Configuration, ScreeningState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), config).orNull()
                    ?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}