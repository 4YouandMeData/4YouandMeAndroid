package org.fouryouandme.auth.screening

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class ScreeningSectionFragment(
    contentLayoutId: Int
) : AuthSectionFragment<ScreeningViewModel>(contentLayoutId) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            screeningFragment(),
            getFactory {
                ScreeningViewModel(
                    navigator,
                    IORuntime,
                    injector.screeningModule(),
                    injector.answerModule()
                )
            }
        )
    }

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