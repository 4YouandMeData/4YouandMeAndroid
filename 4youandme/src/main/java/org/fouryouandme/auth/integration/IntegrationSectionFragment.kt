package org.fouryouandme.auth.integration

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class IntegrationSectionFragment(
    contentLayoutId: Int
) : AuthSectionFragment<IntegrationViewModel>(contentLayoutId) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            integrationFragment(),
            getFactory {
                IntegrationViewModel(
                    navigator,
                    IORuntime,
                    injector.authModule(),
                    injector.integrationModule()
                )
            }
        )
    }

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