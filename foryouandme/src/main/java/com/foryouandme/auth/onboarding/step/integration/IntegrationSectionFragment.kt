package com.foryouandme.auth.onboarding.step.integration

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

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

    fun authNavController(): AuthNavController = integrationFragment().authNavController()

    fun integrationFragment(): IntegrationFragment = find()

    fun integrationNavController(): IntegrationNavController =
        IntegrationNavController(findNavController())

    fun integrationAndConfiguration(block: suspend (Configuration, IntegrationState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController()).orNull()?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}