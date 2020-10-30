package com.fouryouandme.auth.integration

import androidx.navigation.fragment.findNavController
import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.injector
import com.fouryouandme.core.ext.navigator

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
                    injector.integrationModule()
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