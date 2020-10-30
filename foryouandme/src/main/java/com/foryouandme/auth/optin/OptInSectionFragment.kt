package com.foryouandme.auth.optin

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

abstract class OptInSectionFragment(
    contentLayoutId: Int
) : BaseFragment<OptInViewModel>(contentLayoutId) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            optInFragment(),
            getFactory {
                OptInViewModel(
                    navigator,
                    injector.optInModule()
                )
            }
        )
    }

    fun authNavController(): AuthNavController = optInFragment().authNavController()

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