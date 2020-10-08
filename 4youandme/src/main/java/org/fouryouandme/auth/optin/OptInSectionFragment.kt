package org.fouryouandme.auth.optin

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class OptInSectionFragment(
    contentLayoutId: Int
) : AuthSectionFragment<OptInViewModel>(contentLayoutId) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            optInFragment(),
            getFactory {
                OptInViewModel(
                    navigator,
                    IORuntime,
                    injector.optInModule()
                )
            }
        )
    }

    fun optInFragment(): OptInFragment = find()

    fun optInNavController(): OptInNavController =
        OptInNavController(findNavController())

    fun optInAndConfiguration(block: suspend (Configuration, OptInState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController()).orNull()?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}