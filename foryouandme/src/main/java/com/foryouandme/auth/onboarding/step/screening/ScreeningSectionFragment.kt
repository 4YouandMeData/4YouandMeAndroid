package com.foryouandme.auth.onboarding.step.screening

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

abstract class ScreeningSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ScreeningViewModel>(contentLayoutId) {

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

    fun authNavController(): AuthNavController = screeningFragment().authNavController()

    fun screeningFragment(): ScreeningFragment = find()

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