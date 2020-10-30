package com.fouryouandme.auth.screening

import androidx.navigation.fragment.findNavController
import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.injector
import com.fouryouandme.core.ext.navigator

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
                    injector.answerModule()
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